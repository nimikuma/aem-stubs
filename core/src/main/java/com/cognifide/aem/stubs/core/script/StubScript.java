package com.cognifide.aem.stubs.core.script;

import com.cognifide.aem.stubs.core.StubsException;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Optional;

@SuppressWarnings("PMD.DataClass")
public class StubScript {

  private final StubScriptManager manager;

  private final ResourceResolver resourceResolver;

  private final String path;

  private final Binding binding = new Binding();

  private final CompilerConfiguration compilerConfig = new CompilerConfiguration();

  private final GroovyShell shell = new GroovyShell(binding, compilerConfig);

  @SuppressWarnings({"PMD.LoggerIsNotStaticFinal", "PMD.SingularField"})
  private final Logger logger;

  private final RepositoryFacade repository;

  public StubScript(StubScriptManager manager, ResourceResolver resourceResolver, String path) {
    this.manager = manager;
    this.resourceResolver = resourceResolver;
    this.path = path;
    this.logger = LoggerFactory.getLogger(String.format("%s(%s)", getClass().getSimpleName(), path));
    this.repository = new RepositoryFacade(resourceResolver, StringUtils.substringBeforeLast(path, "/"), manager.getRootPath());

    binding.setVariable("script", this);
    binding.setVariable("resourceResolver", resourceResolver);
    binding.setVariable("repository", repository);
    binding.setVariable("logger", logger);
  }

  public Binding getBinding() {
    return binding;
  }

  public CompilerConfiguration getCompilerConfig() {
    return compilerConfig;
  }

  public String getPath() {
    return path;
  }

  public StubScriptManager getManager() {
    return manager;
  }

  public Object run() {
    final Script shellScript = shell.parse(readSourceCode());
    return shellScript.run();
  }

  private Reader readSourceCode() {
    return repository.useStream(path)
      .map(InputStreamReader::new)
      .orElseThrow(() -> new StubsException(String.format("Cannot read stub script '%s'!", path)));
  }
}