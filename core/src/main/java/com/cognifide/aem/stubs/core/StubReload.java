package com.cognifide.aem.stubs.core;

import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang3.time.DurationFormatUtils;

class StubReload {

  private final long startedAt = System.currentTimeMillis();

  protected int scriptsTotal;

  protected int scriptsFailed;

  protected int mappingsTotal;

  protected int mappingsFailed;

  public int scriptsSucceeded() {
    return scriptsTotal - scriptsFailed;
  }

  public int mappingsSucceeded() {
    return mappingsTotal - mappingsFailed;
  }

  public String scriptsPercent() {
    return formatPercent(scriptsSucceeded(), scriptsTotal);
  }

  public String mappingsPercent() {
    return formatPercent(mappingsSucceeded(), mappingsTotal);
  }

  private String formatPercent(int value, int total) {
    double percent;
    if (total > 0) {
      percent = ((double) value) / ((double) total);
    } else {
      percent = 0.0;
    }
    return NumberFormat.getPercentInstance(Locale.US).format(percent);
  }

  public String duration() {
    return DurationFormatUtils.formatDurationHMS(System.currentTimeMillis() - startedAt);
  }

  public String summary() {
    return String.format("AEM Stubs reloaded in %s | Mappings: %s/%s=%s | Scripts: %s/%s=%s",
      duration(),
      mappingsSucceeded(), mappingsTotal, mappingsPercent(),
      scriptsSucceeded(), scriptsTotal, scriptsPercent()
    );
  }

  @Override
  public String toString() {
    return summary();
  }
}
