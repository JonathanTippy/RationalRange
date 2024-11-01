# RangeRational
(WIP) Fuzzy, Fast Rational number in java.

In this number system, epsilon is the smallest between zero and one, and epsilon becomes one at the largest numbers. In normal cases, however, epsilon is exactly zero. With the addition of upper bounds and lower bounds, if a number can be calculated exactly, it will be exact, but if it cannot, its epsilon will grow. 

For the moment, practically, I'm making no guarantees about epsilon at all. This is within spec because numbers are only described with upper and lower bounds. For now, all shifting is done in one shift, while for better epsilons, it should probably be done one bit at a time. 

Unlike with floats, this number system can't represent any number larger than its largest integer. 

### Goodies:
- If an integer can be represented, it can be perfectly divided by any other integer that can be represented. 
- If an operation yields a value whose upper bound and lower bound are the same, the result is exactly correct. 

Because of the distribution of precision, this number system is ideal for, for example, in a graphing calculator, cases of scrolling for up to 30 seconds either in or out from zero. 
It should also be able to handle the dreaded 0.1 + 0.2 very easily 
(no decimals in the from string constructor yet though)
