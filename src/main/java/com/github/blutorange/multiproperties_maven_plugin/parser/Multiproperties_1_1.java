package com.github.blutorange.multiproperties_maven_plugin.parser;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.github.blutorange.multiproperties_maven_plugin.xsd.version_1_1.MultiProperties;

final class Multiproperties_1_1 implements IMultiproperties {
  private final MultiProperties root;

  public Multiproperties_1_1(MultiProperties root) {
    this.root = root;
  }

  @Override
  public String getFileDescription() {
    return root.getDescription();
  }

  @Override
  public String getHandler() {
    return root.getHandler();
  }

  @Override
  public List<HandlerConfiguration> getHandlerConfigurations() {
    return root.getColumns() //
        .getColumn() //
        .stream() //
        .map(column -> new HandlerConfiguration(column.getName(), column.getHandlerConfiguration())).collect(toList());
  }

  @Override
  public List<Item> getItems() {
    final var result = new ArrayList<Item>();
    final var columnKeys = root.getColumns() //
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
        if (values.size() != columnKeys.length) {
          throw new IllegalArgumentException(String.format("Property <%s> must have exactly <%d> value entries.", name, columnKeys.length));
        }
        final var valueMap = new HashMap<String, String>();
        for (var index = 0; index < values.size(); index += 1) {
          final var value = values.get(index);
          if (value.isDisabled()) {
            continue;
          }
          valueMap.put(columnKeys[index], value.getValue());
        }
        final var mapped = new Property(name, disabled, defaultValue, valueMap);
        result.add(mapped);
      }
      else if (record instanceof MultiProperties.Records.Comment) {
        final var comment = (MultiProperties.Records.Comment)record;
        result.add(new Comment(comment.getValue()));
      }
      else if (record instanceof MultiProperties.Records.Empty) {
        result.add(new Empty());
      }
    }
    return result;
  }
}
