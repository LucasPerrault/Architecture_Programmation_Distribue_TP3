Part1 : Theorical Byzantine problem

Question 1:
step 1 - (1,2,3)
step 2 - (1,2,3)
step 3 - (1,2,3)

Yes, the nodes reach an agreement because we don't have any faulty
process. Well, every nodes can communicate with another and none random
value is send.


Question 2:
-  No, the nodes don't reach an agreement, the algorithm failed.
There isn't a majority of 1, 2, 3 during steps. Well, the majority
values are random or undefined.

- In that case, we need one node (non-faulty) more than.  

- 2k + 1 non faulty nodes are required to reach an agreement, where
k represents the faulty nodes. (3k + 1 in total) proved by Lamport.


Part 1.2 : Leader Election with ECHO algorithm

Question 1: 
- The worst case is when you begin with default node 0 because we send messages for each nodes like the example below.

- The best case is when you begin with default max(N) when you haven't failures in your tree.
With the example saw before, max(N) = 7 and we send 0 message because it's already the hightest elector. 



Question 2 :

Equation of the worst case messages send where N= number of nodes and i= incremental node number in the sum :
2*(N-1) + Sum(N-(i+1)) + (N-2)


Demonstration : You have 5 nodes, i[0 to 4] and default node is 0.
messageNumber = 2*(5-1) + Sum(5-([0 to 4] + 1)) + (5-2)
messageNumber = 8 + [5-5 + 5-4 + 5-3 + 5-2 + 5-1] + 3
messageNumber = 8 + 10 + 3
messageNumber = 21.


Step details :

First step :
0 send messages to 1, 2, 3, 4 			| 4 messages
1, 2, 3, 4 send message to 0			| 4 messages

Second step:
1 send message to 2, 3, 4
2 send message to 3, 4
3 send message to 4				| 6 messages

Third step: 
4 send message to 3
3 send message to 2
2 send message to 1				| 3 messages

Last step:
4 send message to 3, 2, 1, 0 			| 4 messages

Total message number send is 21.



 