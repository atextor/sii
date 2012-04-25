Sii (Studying in Ireland)
=========================

Sii is an arcade/old-style-RPG-esque game written in Scala.

Note that several things you see in the game are based on
real events I encountered while studying in Ireland and for
this reason may note make total sense to you if you weren't
there with me. The source code might be interesting
nevertheless :)

Building
--------

You need Apache Ant to build.
Ant targets:
	
	ant compile     - To compile the program
	ant jar         - To create an executable jar file
	ant exe         - To create a self-contained Windows .exe file
	ant run         - To run the compiled game
	ant clean       - Delete all built files

The build process makes use of Proguard (to create a one-in-all
jar file) and Launch4j to create a Windows .exe. Note that building
the jar and exe will only work with Java 6, as the supplied version
of proguard does not work with Java 7.

Author
------
Andreas Textor <textor.andreas@googlemail.com>

Credits for things I used:

 * Music: Kevin MacLeod (incompetech.com)
 * Character sprites based on work by Philipp Lenssen
 * Level 2 tileset by Hermann Hillmann
 * Coin/Balls animation by Marc Russel (gfxlib-fuzed)

