public interface Interlocking 
{

    /**
     * Adds a train to the rail corridor.
     *
     * @param   trainName A String that identifies a given train. Cannot be the same as any other train present.
     * @param   entryTrackSection The id number of the track section that the train is entering into.
     * @param   destinationTrackSection The id number of the track section that the train should exit from.
     * @throws  IllegalArgumentException 
     *              if the train name is already in use, or there is no valid path from the entry to the destination
     * @throws  IllegalStateException 
     *              if the entry track is already occupied
     */
    public void addTrain(String trainName, int entryTrackSection, int destinationTrackSection)
        throws IllegalArgumentException, IllegalStateException;

    /**
     * The listed trains proceed to the next track section.
     * Trains only move if they are able to do so, otherwise they remain in their current section.
     * When a train reaches its destination track section, it exits the rail corridor next time it moves.
     *
     * @param   trainNames The names of the trains to move.
     * @return  The number of trains that have moved.
     * @throws  IllegalArgumentException 
     *              if the train name does not exist or is no longer in the rail corridor
     */
    public int moveTrains(String[] trainNames)
        throws IllegalArgumentException;

    /**
     * Returns the name of the Train currently occupying a given track section
     *
     * @param   trackSection The id number of the section of track.
     * @return  The name of the train currently in that section, or null if the section is empty/unoccupied.
     * @throws  IllegalArgumentException 
     *              if the track section does not exist
     */
    public String getSection(int trackSection)
        throws IllegalArgumentException;

    /**
     * Returns the track section that a given train is occupying
     *
     * @param   trainName The name of the train.
     * @return  The id number of section of track the train is occupying, or -1 if the train is no longer in the rail corridor
     * @throws  IllegalArgumentException 
     *              if the train name does not exist
     */
    public int getTrain(String trainName)
        throws IllegalArgumentException;

}


