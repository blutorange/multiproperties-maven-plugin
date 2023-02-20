package com.github.blutorange.multiproperties_maven_plugin.common;

import java.util.Collection;

/**
 * Utility methods for collections.
 */
public final class CollectionHelper {
  private CollectionHelper() {}

  /**
   * @param collections A list of collections to check, each may be <code>null</code>.
   * @return <code>true</code> if the collection is <code>null</code> or empty.
   */
  public static boolean areAllCollectionsEmpty(Collection<?>... collections) {
    if (collections == null) {
      return true;
    }
    for (final var collection : collections) {
      if (collection != null && !collection.isEmpty()) {
        return false;
      }
    }
    return true;
  }

  /**
   * @param collection A collection to check, may be <code>null</code>.
   * @return <code>true</code> if the collection is <code>null</code> or empty.
   */
  public static boolean isCollectionEmpty(Collection<?> collection) {
    return collection == null || collection.isEmpty();
  }
}
