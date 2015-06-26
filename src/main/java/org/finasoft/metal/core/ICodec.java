package org.finasoft.metal.core;

/**
 * This class is meant to be used as parent class for exchange specific (de)serialization
 * Codecs are providing methods for reading/writing on the wire via streams
 * At this level, we are only providing encoding for primitive types
 * Extending classes are expected to decode actual objects
 */
public interface ICodec  {
}
