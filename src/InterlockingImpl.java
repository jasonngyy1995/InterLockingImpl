import java.util.ArrayList;

// Train direction can be either North or South
enum Direction
{
    North,
    South
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
    int destination_id;
    Direction train_direction;

    Train(String name, Direction dir, int destId)
    {
        this.trainName = name;
        this.train_direction = dir;
        this.destination_id = destId;
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

    Direction getTrain_direction()
    {
        return this.train_direction;
    }
}

abstract class InterlockingImpl implements Interlocking
{
    // Store all the trains in present
    ArrayList<Train> present_train_list = new ArrayList<Train>();
    ArrayList<Section> sections_list = new ArrayList<Section>();
    ArrayList<Integer> marking = new ArrayList<Integer>();

    boolean checkIfNameExists(String name)
    {
        boolean exist = false;
        for (int i = 0; i < present_train_list.size(); i++)
        {
            if (present_train_list.get(i).getTrainName() == name)
            {
                exist = true;
                break;
            }
        }
        return exist;
    }

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

    @Override
    public void addTrain(String trainName, int entryTrackSection, int destinationTrackSection)
    {
        // avoid same train name and invalid path
        boolean name_exist = checkIfNameExists(trainName);
        boolean path_valid = checkValidPath(entryTrackSection, destinationTrackSection);
        if (name_exist == false || path_valid == false)
        {
            throw new IllegalArgumentException();
        }

        // if the entry section is occupied
        if (!sections_list.get(entryTrackSection - 1).occupyingTrain_name.isEmpty())
        {
            throw new IllegalStateException();
        }

        // prevent possible conflict of trains in section 3 and 4
        if ((entryTrackSection == 3 && destinationTrackSection == 4) || (entryTrackSection == 4 && destinationTrackSection == 3))
        {

        }

        // if entry section is 3, can't enter if section 7 or 11 is occupied with a train to opposite direction

        // if entry section is 11, can't enter if section 7 is occupied with a train to opposite direction

        Section enterSection = sections_list.get(entryTrackSection - 1);
        Direction newTrain_dir = enterSection.transitionsList.get(0).train_direction;
        Train newTrain = new Train(trainName, newTrain_dir, destinationTrackSection);

        present_train_list.add(newTrain);
    }

    public void main(String[] args)
    {
        NetInit netInit = new NetInit();
        netInit.init_railway();
        sections_list = netInit.getSectionsList();
    }
}
