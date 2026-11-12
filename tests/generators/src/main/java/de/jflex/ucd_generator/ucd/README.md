<!--
  Copyright 2023, Gerwin Klein, Régis Décamps, Steve Rowe
  SPDX-License-Identifier: CC-BY-SA-4.0
-->

# Representation of Unicode data

## Unicode data

`UnicodeData` is the top level representation of the data in one Unicode
version. Besides the version and the maximum code point, it holds:

* `PropertyNames`
  The aliases of every property name, and the canonical name of every alias,
  e.g. *block➡(blk,block)*
* `PropertyValues`
  Values and aliases for every property,
  e.g. *block→cyrillicsupplement➡(cyrillicsupplement,cyrillicsup,cyrillicsupplementary)*
* `PropertyValueIntervals`
  - `propertyValueIntervals` Intervals for the property values,
    e.g. *age=v11➡(codepointRange1, codepointRange2)*
  - `usedBinaryProperties` Set of normalized names,
    e.g. *{blank, graph, uppercase, etc.}*
  - `usedEnumProperties` Multimap,
    e.g. *wordbreak➡(other, format, midnum, etc.)*
* `CodepointRangeSet` immutable set of ranges of Unicode code points
  * [`CodepointRange`](../../ucd/CodepointRange.java) (and `MutableCodepointRange`) contiguous
    range (start, end)
  * `CodepointRangeSet.Builder` has operations to add and subtract ranges.
* `CaselessMatches` Partitions of code points that are equal when ignoring case

## Version

* `UcdVersions` Map of version to `UcdVersion`.
* [`UcdVersion`](../../ucd/UcdVersion.java) A version and its map ucdFileType➡File.
  - [`UcdFileType`](../../ucd/UcdFileType.java) enum of the file types, e.g. Blocks, PropList,
    UnicodeData.
* [`Version`](../../version/Version.java) major, minor and optional update number
