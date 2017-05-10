### Goal

Given a number of half open ranges `[x, y)`, when provided with number `z`  return number of ranges it is member of.
There are up to million ranges and up to billions requests.

#### Sample ranges implementation

Is a naive implementation with linear performance O(N) of lookup.
It iterates through all ranges at each request.
Moreover, the best case scenario = worst case scenario O(N).

#### Optimisation considerations

The structure is immutable after build and receives orders of magnitude more lookups than it's size.
It would make optimise for quicker lookups by using more efficient data structure with index.

#### Prototype optimised ranges implementation

The idea is to plot all range bound points into sorted tree,
where each point would have indexed count at it's point and after it until next point.

The performance or lookup on sorted tree is O(Log(N)).

Practically on my laptop it handled 1 million ranges sustaining 20 million of lookups per second.

The prototype could be generalised further:
- to work with other kind of data, not just count (generalise data and indexing mechanism);
- to work with other types of keys.

### How to build

App requires scala & sbt installed on machine.

If you don't have it, on a mac you can do via brew:
```
brew install scala
brew install sbt
```

On any platform without scala and sbt you can use docker image:
```
docker run -it --rm -v "$PWD/:/ranges" -w "/ranges" spikerlabs/scala-sbt /bin/bash
```

### How to run test and app

```
sbt test
sbt "run sample_ranges.txt"
```