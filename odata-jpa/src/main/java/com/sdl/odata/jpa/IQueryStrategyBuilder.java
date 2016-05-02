package com.sdl.odata.jpa;

import com.sdl.odata.api.ODataException;
import com.sdl.odata.api.processor.query.strategy.QueryOperationStrategy;

/**
 * Interface for query operation startegy builders.
 */
public interface IQueryStrategyBuilder {
    QueryOperationStrategy build() throws ODataException;
}
