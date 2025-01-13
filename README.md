# RationalRange
(WIP) Honest Rational number in java.

This number system allows you to know how much you know; if a number can't be defined precisely, it will be defined imprecisely in a controlled way. This way accuracy can be preserved while sacrificing precision.

Epsilon is the smallest between zero and one, and epsilon can grow to any size. In many rational cases, however, epsilon is exactly zero. 

With the addition of upper bounds and lower bounds, if a number can be calculated exactly, it will be exact, but if it cannot, its epsilon will grow. The bounds represent an area where the value could be, but the value is actually a single point. The range is not a range where all possible values are valid. If it was used that way, the rounding error would cause the range to expand incorrectly. If there were no rounding error though, it would work correctly

A bound is not really an exact value, but rather a limit; it can approach its value from either direction. This way, 'approaches zero' can be neatly distinguished from 'can be zero'.

Because bounds represent unknowns as to the exact value, when bounds are compared, if they overlap at all it will result in some ambiguity, which is why this project also has quantified booleans. If two bounds are the same and are perfect, it is 50/50. 
(technically speaking the probability can be anything)
note: quantified booleans may not represent the actual probability of the answer falling in an area. Thus for full correctness, they should really be thought of as true or don't know or false.

### The Good:
- If an integer can be represented, it can be perfectly divided by any other integer that can be represented. 
- If an operation yields a value whose upper bound and lower bound are the same (and act to include rather than exclude their value), the result is exactly that value.
- It should also be able to handle the dreaded 0.1 + 0.2 very easily 
(no decimals in the from string constructor yet though)

### The Bad:
- Unlike with floats, this number system can't represent any number larger than its largest integer.
- There is, however, a way to represent "larger than largest known integer", which is approaches /0. In fact its also important that this exists so that rounding will never result in an invalid answer.
- For now, the numerator and denomenator use ints so the precision of non-perfect values is limited.
- inverted values require a comparison to test weather they have passed each other. If this happens, nothing about the answer is known.

### The Weird:
- The upper bound can be lower than the lower bound. This is caused when the reciprocal is taken of a value which may be positive or may be negative. It is intended behavior.
- in the case of an included zero it results in a very nice "no value" behavior, the upper bound being negative unbounded inclusive, and the lower being positive unbounded inclusive, which basically means "definitely not any known number" because any known number will eventually be excluded.
- zero over zero means all is lost: no information is known about the number.
- x over zero is defined; we start with zero. the lower bound is a limit which approaches zero from below, and the upper bound
approaches zero from above. Now when you take the reciprocal, the upper bound approaches infinity, and the lower bound approaches negative infinity, and the bound trade places. This results in a behavior which makes sense: all known numbers are excluded.