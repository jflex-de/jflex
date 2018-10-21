# Class dependency analysis

**Objective**:

How to analyze class dependencies.

**Prerequisite**:

* [degraph][degraph]
* [yEd][yed]

## Instructions

### Build JFlex

With bazel:

    bazel build //jflex

### Run degraph

    degraph -f scripts/degraph/dep-graph.cfg


This generates a graph in `/tmp/jflex-packages.graphml`

### Open the graph

**Select Layout** -> **Hierarchical …**
  * **General**
    * **Orientation**: **Left to Right**
  * **Edges**
    * **Routing Style**: **Polyline**

__Tip__: Click on the Dock button. This will put the dialog in the side bar, so that we can re-apply
these settings easily.

Click on the **Play** button.

### Advanced

*  Custom slicing
   http://blog.schauderhaft.de/degraph/documentation.html#slicing


[degraph]: http://blog.schauderhaft.de/degraph/
[yed]: https://www.yworks.com/products/yed
