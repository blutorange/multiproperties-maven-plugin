package com.github.blutorange.multiproperties_maven_plugin.handler;

/**
 * The {@link HandlerImplementor output handler} which delegates to the handler as defined in the multiproperties file.
 */
public final class DefaultHandler extends AbstractHandler {
  private Boolean removeFirstPathSegment;

  /**
   * Creates a new empty default handler instance.
   */
  public DefaultHandler() {
    super(DefaultHandlerImplementor.class);
  }

  /**
   * @return When <code>true</code>, remove the first path segment from the multiproperties output file path. The first
   * segment is usually the name of the Eclipse project, which may be different from the name of the folder itself.
   */
  public boolean isRemoveFirstPathSegment() {
    return removeFirstPathSegment != null ? removeFirstPathSegment.booleanValue() : true;
  }

  /**
   * @param removeFirstPathSegment When <code>true</code>, remove the first path segment from the multiproperties output
   * file path. The first segment is usually the name of the Eclipse project, which may be different from the name of
   * the folder itself.
   */
  public void setRemoveFirstPathSegment(boolean removeFirstPathSegment) {
    this.removeFirstPathSegment = removeFirstPathSegment;
  }
}
