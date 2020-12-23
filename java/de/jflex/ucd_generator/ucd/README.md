# Representation of Unicode data

## Unicode data
`UnicodeData` is the top level representation of the data in one Unicode
version.

* `PropertyNameNormalizer`
  Multimap of Property names and to name and aliases)
  e.g. *block➡(blk,block)*
  TODO: Rename PropertyNames{bg:wheat}]
* `PropertyValues`
  Values and aliases for every property
  e.g. *block→cyrillicsupplement➡(cyrillicsupplement,cyrillicsup,cyrillicsupplementary)*
* `PropertyValueIntervals`
  - `intervals` Intervals for the property values.
    e.g. *age=v11➡(codepointRange1, codepointRange2)*
  - `usedBinaryProperties` Set
    e.g. *{blank, graph, uppercase, etc.}*
  - `usedEnumProperties` Multimap
    e.g. *wordpreak➡(other, format, midnum, etc.)*
* `CodepointRange` immutable range (start, end)
* `CaselessMatches` Partitions

<img src="https://yuml.me/jflex/ucd.png" width="1048" height="285">

## Version
* `UcdVersion` Map version to map ucdType➡File.
  - `UddType` enum of the file types, e.g Block, PropList, UnicodeData.
* `Version` major minor versioning
