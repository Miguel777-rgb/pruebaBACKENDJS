package com.maki.pompompurin.model;

// Un record genera solo el constructor, los getters, equals, hashCode y toString
public record Maki(Long id, String name, double price, int pieces, String ingredients) {
}
