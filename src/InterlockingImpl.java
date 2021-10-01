import java.util.ArrayList;

// Train direction can be either North or South
enum Direction
{
    North,
    South,
    BiDirection
}

// Track section (= a place in the Petri Net)
class Section
{
    int sectionId;
    String occupyingTrain_name;
    ArrayList<Transition> transitionsList;
    ArrayList<Section> possibleDestination;

    Section(int sectionId)
    {
        this.sectionId = sectionId;
    }

    void setOccupyingTrain_name(String train_name)
    {
        this.occupyingTrain_name = train_name;
    }

    String getOccupyingTrain_name()
    {
        return this.occupyingTrain_name;
    }

    void addTransition(Transition tran)
    {
        this.transitionsList.add(tran);
    }

    void addDestination(Section dest)
    {
        this.possibleDestination.add(dest);
    }
}

// Track transition through with train can move to another track section
class Transition
{
    Direction train_direction;
    Section next_section;

    Transition(Direction dir, Section next_section)
    {
        this.train_direction = dir;
        this.next_section = next_section;
    }

    Direction getTransition_dir()
    {
        return this.train_direction;
    }

    Section getNext_section()
    {
        return this.next_section;
    }
}

class Train
{
    String trainName;
    int occupying_SectionId;
    int dest_SectionId;
    Direction train_direction;

    Train(String name, Direction dir, int destId)
    {
        this.trainName = name;
        this.train_direction = dir;
        this.dest_SectionId = destId;
    }

    void setOccupying_SectionId(int id)
    {
        this.occupying_SectionId = id;
    }

    String getTrainName()
    {
        return this.trainName;
    }

    int getOccupying_SectionId()
    {
        return this.occupying_SectionId;
    }

    int getDest_SectionId()
    {
        return this.dest_SectionId;
    }

    Direction getTrain_direction()
    {
        return this.train_direction;
    }
}

abstract class InterlockingImpl implements Interlocking
{
    // Store all the trains in present
    ArrayList<Train> present_train_list = new ArrayList<Train>();
    ArrayList<Train> exited_train_list = new ArrayList<Train>();
    ArrayList<Section> sections_list = new ArrayList<Section>();
    ArrayList<PointMachine> pointMachines_list = new ArrayList<PointMachine>();

    // function to call at first to initialize the petri net of the railway system
    public void initialize_PetriNet()
    {
        NetInit netInit = new NetInit();
        netInit.init_railway();
        sections_list = netInit.getSectionsList();


    }

    // function to check if the train name is unique
    boolean checkIfTrainExists(String name)
    {
        boolean exist = false;
        for (int i = 0; i < present_train_list.size(); i++)
        {
            if (present_train_list.get(i).getTrainName().equals(name))
            {
                exist = true;
                break;
            }
        }
        return exist;
    }

    // check if the input of addTrain() is a valid path
    boolean checkValidPath(int entry_section, int dest_section)
    {
        boolean valid = false;
        Section entrySection = sections_list.get(entry_section - 1);
        for (Section sec: entrySection.possibleDestination)
        {
            if (dest_section == sec.sectionId)
            {
                valid = true;
                break;
            }
        }
        return valid;
    }

    // function to find the index of a train in present_train_list
    int getTrainPos(String name)
    {
        int pos;
        for (int i = 0; i < present_train_list.size(); i++)
        {
            if (present_train_list.get(i).getTrainName().equals(name))
            {
                pos = i;
                return pos;
            }
        }
        return -1;
    }

    @Override
    public void addTrain(String trainName, int entryTrackSection, int destinationTrackSection)
    {
        // avoid same train name and invalid path
        boolean name_exist = checkIfTrainExists(trainName);
        boolean path_valid = checkValidPath(entryTrackSection, destinationTrackSection);
        if (name_exist == true || path_valid == false)
        {
            throw new IllegalArgumentException();
        }

        // if the entry section is occupied
        if (!sections_list.get(entryTrackSection - 1).occupyingTrain_name.isEmpty())
        {
            throw new IllegalStateException();
        }

        Section enterSection = sections_list.get(entryTrackSection - 1);
        Direction newTrain_dir = enterSection.transitionsList.get(0).train_direction;
        Train newTrain = new Train(trainName, newTrain_dir, destinationTrackSection);

        present_train_list.add(newTrain);
    }

    public int moveSingleTrain(String trainName)
    {


    }

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
    @Override
    public int moveTrains(String[] trainNames)
    {
        // total number of moved successfully trains
        int movedTrains = 0;

        for (String train: trainNames)
        {
            boolean validTrain = checkIfTrainExists(train);
            if (validTrain == false)
            {
                throw new IllegalArgumentException();
            }

            int successfulPass = moveSingleTrain(train);
            movedTrains += successfulPass;
        }

        return movedTrains;
    }

    @Override
    public int getTrain(String trainName)
    {
        int trainPos = getTrainPos(trainName);
        if (trainPos == -1)
        {
            throw new IllegalArgumentException();
        }

        int occupying_section = present_train_list.get(trainPos).getOccupying_SectionId();
        return occupying_section;
    }

    @Override
    public String getSection(int trackSection)
    {
        if (trackSection < 1 || trackSection > 11)
        {
            throw new IllegalArgumentException();
        }

        String occupying_trainName = sections_list.get(trackSection - 1).getOccupyingTrain_name();
        return occupying_trainName;
    }

}


// ************************************ Path Checking ******************************************

//    // prevent possible conflict of trains in section 3 and 4
//    String returnOccupyingTrain = sections_list.get(destinationTrackSection - 1).getOccupyingTrain_name();
//
//    if (entryTrackSection == 3 && destinationTrackSection == 4 && !returnOccupyingTrain.equals(null))
//    {
//        int trainPos = getTrainPos(returnOccupyingTrain);
//        if (present_train_list.get(trainPos).getDestination_id() == 3)
//        {
//            System.out.println("Train to section 3 was added in section 4 already, moving from section 3 to section 4 is not allowed.");
//            throw new IllegalArgumentException();
//        }
//    }
//
//    if (entryTrackSection == 4 && destinationTrackSection == 3 && !returnOccupyingTrain.equals(null))
//    {
//        int trainPos = getTrainPos(returnOccupyingTrain);
//
//        if (present_train_list.get(trainPos).getDestination_id() == 4)
//        {
//            System.out.println("Train to section 4 was added in section 3 already, entry to section 4 blocked.");
//            throw new IllegalArgumentException();
//        }
//    }

// if train enters from section 3 to section 11, can't enter if section 7 or 11 is occupied with a train to opposite direction
//        if (entryTrackSection == 3 && destinationTrackSection != 4)
//                {
//                int trainPos;
//                if (!sections_list.get(6).getOccupyingTrain_name().equals(null))
//                {
//                trainPos = getTrainPos(sections_list.get(6).getOccupyingTrain_name());
//                if (present_train_list.get(trainPos).getTrain_direction() == Direction.North)
//                {
//                System.out.println("Train is already coming, entry to section 3 blocked.");
//                throw new IllegalArgumentException();
//                }
//                }
//
//                if (!sections_list.get(10).getOccupyingTrain_name().equals(null))
//                {
//                trainPos = getTrainPos(sections_list.get(10).getOccupyingTrain_name());
//                if (present_train_list.get(trainPos).getTrain_direction() == Direction.North)
//                {
//                System.out.println("Train is already coming, entry to section 3 blocked.");
//                throw new IllegalArgumentException();
//                }
//                }
//                }
//
//                // if entry section is 11, can't enter if section 7 is occupied with a train to opposite direction, or another train enters from section 3 to section 11
//                if (entryTrackSection == 11)
//                {
//                int trainPos;
//                if (!sections_list.get(6).getOccupyingTrain_name().equals(null))
//                {
//                trainPos = getTrainPos(sections_list.get(6).getOccupyingTrain_name());
//                if (present_train_list.get(trainPos).getTrain_direction() == Direction.South)
//                {
//                System.out.println("Train is already coming, entry to section 11 blocked.");
//                throw new IllegalArgumentException();
//                }
//                }
//
//                if (!sections_list.get(2).getOccupyingTrain_name().equals(null))
//                {
//                trainPos = getTrainPos(sections_list.get(10).getOccupyingTrain_name());
//                if (present_train_list.get(trainPos).getTrain_direction() == Direction.South)
//                {
//                System.out.println("Train is already coming, entry to section 11 blocked.");
//                throw new IllegalArgumentException();
//                }
//                }
//                }