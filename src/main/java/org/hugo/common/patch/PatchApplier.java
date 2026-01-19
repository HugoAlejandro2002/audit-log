package org.hugo.common.patch;

public interface PatchApplier<E, R> {
    PatchResult apply(E entity, R request);
}
