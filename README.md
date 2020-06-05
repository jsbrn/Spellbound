# Spellbound

[Download the most recent public build from itch.io](https://computerology.itch.io/spellbound)

*As a student of magic, you have the unique ability to create spells of your own design, using techniques taught to you by the Headmaster Wizard, who oversees the day-to-day operations of the Arcane University. Eventually he begins to take a keen interest in your talents, and one day decides to teach you a special spell to help him run a very special errand.*

### Overview

In Spellbound, you learn and discover techniques, which can be combined to create custom spells using a dynamic particle and magic system. Each technique affects a specific property or behavior of the spell. For example, a basic fireball spell might be created by combining the Propulsion, Collision and Ignition techniques. That is, the spell propels itself towards a target, then collides with and ignites it in a burst of flame. Chain two or more spells together to create powerful combinations, or merge them to cast two simultaneously.

Explore a large, open world with dungeons, caves and monsters. Collect magic crystals to fuel your spellcrafting, or sell them to a shopkeeper for gold coins.

### Planned Features

* Create custom spells using a dynamic and modular magic system
* Defeat monsters and bandits in a randomly generated world with dungeons, caves and more
* A main quest that focuses on saving the village from impending doom
* Randomly generated quests from villagers and other NPCs
* Unique puzzles and secrets scattered around the world
   
### How to Contribute

###### Setup

Make sure you have at least JDK 1.8. Spellbound is a Maven project, so you can open it in any IDE you choose and import the dependencies.

Maven will unpack the LWJGL natives to `target/natives` when you run `mvn package`. Point LWJGL to them by setting the VM arguments to `-Djava.library.path=target/natives`.

Slick2D depends on your system having `javaws.jar`. It is not supplied in the OpenJDK, so if you are running Linux you will need to install NetX.

Do `sudo apt-get install icedtea-netx-common` and copy `/usr/share/icedtea-web/netx.jar` to `/usr/lib/jvm/java-8-openjdk-amd64/jre/lib` (or wherever the `jre/lib` folder is for the JDK version you have installed).

This should be enough to get you up and running.

###### Launch Notes

* The root directory (`{ROOT_DIRECTORY}`), where save files, settings and audio assets are kept, is referenced as `Assets.ROOT_DIRECTORY` and defaults to `{USER_HOME}/.sbclassic/`.

* The game takes one optional argument on launch which is a boolean indicating whether or not music is to be loaded.
This saves a good chunk of time if you aren't touching the music manager at all. It defaults to `true`.

* If the `{ROOT_DIRECTORY}/assets/sounds` is empty, the game will download and unpack the assets from a server automatically. 
If the folder has any contents, the downloading will be skipped and the game will attempt to load the required sounds. 

###### Contribution Guidelines

1. Please fork `dev`, not `master`. `dev` is where the bleeding edge changes are made, and it's where you should pull request to when the time comes.
2. If you see a bug or want to suggest a feature, open an issue.
3. If you want to start contributing, check the Issues tab to see if there's anything you might want to tackle.