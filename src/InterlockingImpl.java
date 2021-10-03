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
    ArrayList<Transition> transitionsList = new ArrayList<Transition>();
    ArrayList<Section> possibleDestination = new ArrayList<Section>();;

    Section(int sectionId, String occ_train)
    {
        this.sectionId = sectionId;
        this.occupyingTrain_name = occ_train;
    }

    int getSectionId()
    {
        return this.sectionId;
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

    ArrayList<Transition> getTransitionsList()
    {
        return this.transitionsList;
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
    int next_section_id;

    Transition(Direction dir, int next_section_id)
    {
        this.train_direction = dir;
        this.next_section_id = next_section_id;
    }

    Direction getTransition_dir()
    {
        return this.train_direction;
    }

    int getNext_section_id()
    {
        return this.next_section_id;
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

    String getTrainName()
    {
        return this.trainName;
    }

    void setOccupying_SectionId(int id)
    {
        this.occupying_SectionId = id;
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

public class InterlockingImpl implements Interlocking
{
    // Store all the trains in present
    ArrayList<Train> present_train_list = new ArrayList<Train>();
    ArrayList<Train> exited_train_list = new ArrayList<Train>();
    ArrayList<Section> sections_list = new ArrayList<Section>();
    PetriNet petriNet = new PetriNet();

    // function to call at first to initialize the petri net of the railway system
    public void initialize_PetriNet()
    {
        petriNet.init_railway();
        sections_list = petriNet.getSectionsList();

        petriNet.init_firingPolicies();
        petriNet.init_pointMachines();

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
        enterSection.setOccupyingTrain_name(trainName);
        newTrain.setOccupying_SectionId(entryTrackSection);

        present_train_list.add(newTrain);
    }

    // get the next section to move to
    int nextSection_toMove(String trainName)
    {
        int id_toReturn = -1;
        int trainPos = getTrainPos(trainName);
        Train moving_train = present_train_list.get(trainPos);

        int current_sec = moving_train.getOccupying_SectionId();
        ArrayList<Transition> sec_tran = sections_list.get(current_sec - 1).getTransitionsList();

        for (int i = 0; i < sec_tran.size(); i++)
        {
            if (sec_tran.get(i).getNext_section_id() == moving_train.getDest_SectionId())
            {
                Transition next_tran = sec_tran.get(i);
                id_toReturn = next_tran.getNext_section_id();
                break;

            } else if (sec_tran.get(i).getTransition_dir() == moving_train.getTrain_direction())
            {
                Transition next_tran = sec_tran.get(i);
                id_toReturn = next_tran.getNext_section_id();
            }
        }

        return id_toReturn;
    }

    boolean checkIfSectionEmpty(int sectionId)
    {
        Section sec = sections_list.get(sectionId - 1);
        if (sec.getOccupyingTrain_name().equals(""))
        {
            return true;
        } else {
            return false;
        }
    }

    public int moveSingleTrain(String trainName)
    {
        int next_section_id = nextSection_toMove(trainName);
        if (next_section_id == -1)
        {
            System.out.println("Error in getting next section, program stops.");
            throw new IllegalStateException();
        }

        int trainPos = getTrainPos(trainName);
        Train moving_train = present_train_list.get(trainPos);
        int current_sec = moving_train.getOccupying_SectionId();

        boolean canPass = true;
        String policy_to_check;

        if (current_sec == 7 && next_section_id == 3 || current_sec == 4 && next_section_id == 3)
        {
            policy_to_check = "S"+next_section_id+"S"+current_sec;
        } else {
            policy_to_check = "S"+current_sec+"S"+next_section_id;
        }

        ArrayList<FiringPolicy> policiesList = petriNet.getPoliciesList();

        for (int i = 0; i < policiesList.size(); i++)
        {
            FiringPolicy policy = policiesList.get(i);
            if (policy.getPolicy_name().equals(policy_to_check) && policy.getEnabled() == false)
            {
                canPass = false;
                break;
            }
        }

        if (canPass == true)
        {
            boolean isEmpty = checkIfSectionEmpty(next_section_id);
            if (isEmpty == true)
            {
                sections_list.get(next_section_id - 1).setOccupyingTrain_name(trainName);
                sections_list.get(current_sec - 1).setOccupyingTrain_name("");
                moving_train.setOccupying_SectionId(next_section_id);
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }

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
        }

        // Firstly, all trains at destination are exited
        for (int i = 0; i < present_train_list.size(); i++)
        {
            int currentSectionId = present_train_list.get(i).getOccupying_SectionId();
            if (currentSectionId == present_train_list.get(i).getDest_SectionId())
            {
                sections_list.get(currentSectionId - 1).setOccupyingTrain_name("");
                exited_train_list.add(present_train_list.get(i));
                present_train_list.remove(i);
            }
        }

        // update firing policies
        petriNet.update_PointMachine(sections_list);

        // move a single train
        for (String train : trainNames)
        {
            movedTrains += moveSingleTrain(train);
        }

        petriNet.reset_to_default();

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