package com.github.blutorange.multiproperties_maven_plugin.parser;

import static com.github.blutorange.multiproperties_maven_plugin.common.StringHelper.defaultIfEmpty;
import static java.util.stream.Collectors.toMap;

import java.util.HashMap;
import java.util.Map;

import com.github.blutorange.multiproperties_maven_plugin.xsd.version_1_2.MultiProperties;
import com.github.blutorange.multiproperties_maven_plugin.xsd.version_1_2.MultiProperties.Records.Property.Value;

final class Multiproperties_1_2 implements IMultiproperties {
  private final MultiProperties root;

  public Multiproperties_1_2(MultiProperties root) {
    this.root = root;
  }

  @Override
  public String getHandler() {
    return root.getHandler();
  }

  @Override
  public Map<String, String> getHandlerConfigurations() {
    return root.getColumns() //
        .getColumn() //
        .stream() //
        .collect(toMap( //
            column -> column.getName(), //
            column -> column.getHandlerConfiguration() //
        ));
  }

  @Override
  public Map<String, Map<String, String>> getResolvedProperties() {
    final var result = new HashMap<String, Map<String, String>>();
    final var languages = root.getColumns() //
        .getColumn() //
        .stream() //
        .map(column -> column.getName()) //
        .toArray(String[]::new);
    for (final var record : root.getRecords().getPropertyOrCommentOrEmpty()) {
      if (record instanceof MultiProperties.Records.Property) {
        final var property = (MultiProperties.Records.Property)record;
        final var name = property.getName();
        final var values = property.getValue();
        final var defaultValue = property.getDefaultValue();
        final var disabled = property.isDisabled();
        if (disabled) {
          continue;
        }
        if (values.size() != languages.length) {
          throw new IllegalArgumentException(String.format("Property <%s> must have exactly <%d> value entries.", name, languages.length));
        }
        final var langMap = result.computeIfAbsent(name, $ -> new HashMap<>());
        for (var index = 0; index < values.size(); index += 1) {
          final var language = languages[index];
          final var value = resolveValue(values.get(index), defaultValue);
          langMap.put(language, defaultIfEmpty(value, ""));
        }
      }
    }
    return result;
  }

  private String resolveValue(Value value, String defaultValue) {
    return value.isDisabled() ? defaultValue : value.getValue();
  }
}
