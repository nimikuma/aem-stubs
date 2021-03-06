package com.cognifide.aem.stubs.core.util;

import static java.util.Collections.singletonMap;
import static org.apache.sling.api.resource.ResourceResolverFactory.SUBSERVICE;

import java.util.function.Consumer;
import java.util.function.Function;

import com.cognifide.aem.stubs.core.StubsException;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = ResolverAccessor.class)
public class ResolverAccessor {

  private static final Logger LOG = LoggerFactory.getLogger(ResolverAccessor.class);

  @Reference
  private ResourceResolverFactory factory;

  public <T> T resolve(Function<ResourceResolver, T> function) {
    try (ResourceResolver resolver = retrieveResourceResolver()) {
      return function.apply(resolver);
    } catch (LoginException e) {
      LOG.error("Cannot create resource resolver for mapper service.", e);
      throw new StubsException(
        "Cannot create resource resolver for mapper service. Is service user mapper configured?", e
      );
    }
  }

  public void consume(Consumer<ResourceResolver> consumer) {
    resolve(resolver -> {
      consumer.accept(resolver);
      return null;
    });
  }

  private ResourceResolver retrieveResourceResolver() throws LoginException {
    return factory.getServiceResourceResolver(singletonMap(SUBSERVICE, "com.cognifide.aem.stubs"));
  }
}
