package com.github.blutorange.multiproperties_maven_plugin.handler;

import static com.github.blutorange.multiproperties_maven_plugin.handler.HandlerConfigurationParser.defaultHandler;

/**
 * Implementor for the {@link DefaultHandler}.
 */
public final class DefaultHandlerImplementor implements HandlerImplementor<DefaultHandler> {
  @Override
  public String getName() {
    return "Default Multiproperties Handler";
  }

  @Override
  public void handleProperties(HandlerImplementorContext<DefaultHandler> context) throws Exception {
    final var delegate = DefaultHandlerFactory.implementorForName(context.getDefaultHandlerName());
    executeDelegate(delegate, context);
  }

  @Override
  public DefaultHandler parseConfig(String configuration) {
    return defaultHandler(configuration);
  }

  private <C extends Handler> void executeDelegate(HandlerImplementor<C> delegate, HandlerImplementorContext<DefaultHandler> ctx) throws Exception {
    final var config = ctx.getConfiguration();
    final var delegateConfig = delegate.parseConfig(ctx.getDefaultHandlerConfigurationString());
    final var delegateContext = ctx.derive(delegateConfig) //
        .withRemoveFirstPathSegment(config.isRemoveFirstPathSegment()) //
        .build();
    ctx.getLogger().info(String.format("Delegating to output handler <%s>", delegate.getName()));
    delegate.handleProperties(delegateContext);
  }
}
