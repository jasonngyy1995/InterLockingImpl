import java.util.ArrayList;

// Train direction can be either North or South
enum Direction
{
    North,
    South,
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
    int moved_times;

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

    int getMoved_times()
    {
        return this.moved_times;
    }

    void incrementMovedTimes_By1()
    {
        this.moved_times = 1;
    }

    void reset_movedTimes_zero()
    {
        this.moved_times = 0;
    }
}

public class InterlockingImpl implements Interlocking
{
    // Store all the trains in present
    ArrayList<Train> present_train_list = new ArrayList<Train>();
    ArrayList<Train> exited_train_list = new ArrayList<Train>();
    ArrayList<Section> sections_list = new ArrayList<Section>();
    PetriNet petriNet;

    InterlockingImpl()
    {
        this.petriNet = new PetriNet();
        petriNet.init_railway();
        this.sections_list = petriNet.getSectionsList();

        petriNet.init_firingPolicies();
        petriNet.init_pointMachines();
    }

    boolean checkIfTrainInRailCorridor(String name)
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

    // function to check if the train name is unique
    boolean checkIfTrainNameUsed(String name)
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

        for (int i = 0; i < exited_train_list.size(); i++)
        {
            if (exited_train_list.get(i).getTrainName().equals(name))
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

    boolean check_ifTrainExistsButLeft(String trainName)
    {
        for (int i = 0; i < exited_train_list.size(); i++)
        {
            if (exited_train_list.get(i).getTrainName().equals(trainName))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addTrain(String trainName, int entryTrackSection, int destinationTrackSection)
    {
        // avoid same train name and invalid path
        boolean name_exist = checkIfTrainNameUsed(trainName);
        boolean path_valid = checkValidPath(entryTrackSection, destinationTrackSection);
        if (name_exist == true || path_valid == false)
        {
            throw new IllegalArgumentException();
        }

        // if the entry section is occupied
        if (!sections_list.get(entryTrackSection - 1).occupyingTrain_name.equals(""))
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

    // trains section 6 and 1 always have priority to move first
    String[] sort_train_list(String[] original_list)
    {
        String[] listToReturn;

        for (int i = 0; i < original_list.length; i++)
        {
            if (getTrain(original_list[i]) == 6)
            {
                String passenger_train_1 = original_list[i];
                int p1_index = i;

                if (getTrain(original_list[0]) != 1)
                {
                    String tmp2 = original_list[0];
                    original_list[0] = passenger_train_1;
                    original_list[p1_index] = tmp2;

                } else {
                    String tmp2 = original_list[1];
                    original_list[1] = passenger_train_1;
                    original_list[p1_index] = tmp2;
                }
            }

            if (getTrain(original_list[i]) == 1)
            {
                String passenger_train_2 = original_list[i];
                int p2_index = i;

                if (getTrain(original_list[0]) != 6)
                {
                    String tmp2 = original_list[0];
                    original_list[0] = passenger_train_2;
                    original_list[p2_index] = tmp2;

                } else {
                    String tmp2 = original_list[1];
                    original_list[1] = passenger_train_2;
                    original_list[p2_index] = tmp2;
                }
            }
        }

        listToReturn = original_list;
        return listToReturn;
    }

    // function to move each train in the list
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
                moving_train.incrementMovedTimes_By1();
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

    @Override
    public int moveTrains(String[] trainNames)
    {
        // total number of moved successfully trains
        int movedTrains = 0;

        for (String train: trainNames)
        {
            boolean validTrain = checkIfTrainInRailCorridor(train);
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

        // reset their moving times to 0
        for (String scanning_train : trainNames)
        {
            int pos = getTrainPos(scanning_train);
            Train train = present_train_list.get(pos);
            train.reset_movedTimes_zero();
        }

        // update firing policies
        petriNet.update_PointMachine(sections_list, present_train_list);

        // move a single train
        String[] sortedlist = sort_train_list(trainNames);
        int first_round_count = 0;

        for (int i = 0; i < sortedlist.length; i++)
        {
            int count = moveSingleTrain(sortedlist[i]);
            first_round_count += count;
            movedTrains += count;
            petriNet.update_PointMachine(sections_list, present_train_list);
        }

        while (first_round_count > 0)
        {
            first_round_count = 0;
            for (int i = 0; i < sortedlist.length; i++)
            {
                int pos = getTrainPos(sortedlist[i]);
                Train current_train = present_train_list.get(pos);

                if (current_train.getMoved_times() == 0)
                {
                    int count = moveSingleTrain(sortedlist[i]);
                    first_round_count += count;
                    movedTrains += count;
                    petriNet.update_PointMachine(sections_list, present_train_list);
                }
            }
        }

        return movedTrains;
    }

    @Override
    public int getTrain(String trainName)
    {
        int trainPos = getTrainPos(trainName);
        boolean exitedTrain = check_ifTrainExistsButLeft(trainName);
        if (trainPos == -1 && exitedTrain == false)
        {
            throw new IllegalArgumentException();
        }

        if (exitedTrain)
        {
            return -1;
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

        if (!occupying_trainName.equals(""))
        {
            return occupying_trainName;
        } else {
            return null;
        }

    }

}