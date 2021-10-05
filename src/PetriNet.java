import java.util.ArrayList;

class FiringPolicy
{
    String policy_name;
    Direction direction;
    boolean enabled;

    FiringPolicy(String policy_name, Direction direction)
    {
        this.policy_name = policy_name;
        this.direction = direction;
    }

    String getPolicy_name()
    {
        return this.policy_name;
    }

    boolean getEnabled()
    {
        return this.enabled;
    }

    void setEnabled(boolean permission)
    {
        this.enabled = permission;
    }
}

class PointMachine
{
    int machine_id;
    ArrayList<FiringPolicy> controlled_policies = new ArrayList<FiringPolicy>();
    String current_enablePolicy;

    PointMachine(int assigned_id)
    {
        this.machine_id = assigned_id;
    }

    int getMachine_id()
    {
        return this.machine_id;
    }

    void add_policy(FiringPolicy policy)
    {
        this.controlled_policies.add(policy);
    }

    ArrayList<FiringPolicy> getControlled_policies()
    {
        return this.controlled_policies;
    }

    void setCurrent_enablePolicy(String policyName)
    {
        this.current_enablePolicy = policyName;
    }

    String getCurrent_enablePolicy()
    {
        return this.current_enablePolicy;
    }

    void default_enabled_policy(int index, boolean permission)
    {
        this.controlled_policies.get(index).setEnabled(permission);
    }
}

public class PetriNet
{
    // Store the railway system
    ArrayList<Section> sectionsList = new ArrayList<Section>();
    ArrayList<FiringPolicy> policiesList = new ArrayList<FiringPolicy>();
    ArrayList<PointMachine> pointMachineList = new ArrayList<PointMachine>();

    void init_railway()
    {
        // section 1 - 11 are track sections
        for (int i = 1; i < 12; i++)
        {
            Section section = new Section(i,"");
            sectionsList.add(section);
        }

        // ----------------------------------Initialize Passenger Line entered with section 1 or 9------------------------------------
        // initialize section 1
        Section section_1 = sectionsList.get(0);
        Transition sect_1_tran = new Transition(Direction.South, sectionsList.get(4).getSectionId());
        section_1.addTransition(sect_1_tran);
        section_1.addDestination(sectionsList.get(7));
        section_1.addDestination(sectionsList.get(8));

        // initialize section 5
        Section section_5 = sectionsList.get(4);
        Transition sect_5_tran_1 = new Transition(Direction.South, sectionsList.get(7).getSectionId());
        Transition sect_5_tran_2 = new Transition(Direction.South, sectionsList.get(8).getSectionId());
        section_5.addTransition(sect_5_tran_1);
        section_5.addTransition(sect_5_tran_2);

        // initialize section 9
        Section section_9 = sectionsList.get(8);
        Transition sect_9_tran = new Transition(Direction.North, sectionsList.get(5).getSectionId());
        section_9.addTransition(sect_9_tran);
        section_9.addDestination(sectionsList.get(1));

        // ----------------------------------Initialize Passenger Line Entered with section 10------------------------------------
        // initialize section 10
        Section section_10 = sectionsList.get(9);
        Transition sect_10_tran = new Transition(Direction.North, sectionsList.get(5).getSectionId());
        section_10.addTransition(sect_10_tran);
        section_10.addDestination(sectionsList.get(1));

        // initialize section 6
        Section section_6 = sectionsList.get(5);
        Transition sect_6_tran = new Transition(Direction.North, sectionsList.get(1).getSectionId());
        section_6.addTransition(sect_6_tran);

        // ----------------------------------Initialize Freight Line-----------------------------------
        // initialize section 3
        Section section_3 = sectionsList.get(2);
        Transition sect_3_tran_1 = new Transition(Direction.South, sectionsList.get(3).getSectionId());
        Transition sect_3_tran_2 = new Transition(Direction.South, sectionsList.get(6).getSectionId());
        section_3.addTransition(sect_3_tran_1);
        section_3.addTransition(sect_3_tran_2);
        section_3.addDestination(sectionsList.get(3));
        section_3.addDestination(sectionsList.get(10));

        // initialize section 4
        Section section_4 = sectionsList.get(3);
        Transition sect_4_tran = new Transition(Direction.North, sectionsList.get(2).getSectionId());
        section_4.addTransition(sect_4_tran);
        section_4.addDestination(sectionsList.get(2));

        // initialize section 7
        Section section_7 = sectionsList.get(6);
        Transition sect_7_tranToS = new Transition(Direction.South, sectionsList.get(10).getSectionId());
        Transition sect_7_tranToN = new Transition(Direction.North, sectionsList.get(2).getSectionId());
        section_7.addTransition(sect_7_tranToS);
        section_7.addTransition(sect_7_tranToN);

        // initialize section 11
        Section section_11 = sectionsList.get(10);
        Transition sect_11_tran = new Transition(Direction.North, sectionsList.get(6).getSectionId());
        section_11.addTransition(sect_11_tran);
        section_11.addDestination(sectionsList.get(2));
    }

    ArrayList<Section> getSectionsList()
    {
        return sectionsList;
    }

    void init_firingPolicies()
    {
        FiringPolicy S3S7 = new FiringPolicy("S3S7", Direction.BiDirection);
        policiesList.add(S3S7);
        FiringPolicy S3S4 = new FiringPolicy("S3S4", Direction.BiDirection);
        policiesList.add(S3S4);
        FiringPolicy S6S2 = new FiringPolicy("S6S2", Direction.North);
        policiesList.add(S6S2);
        FiringPolicy S1S5 = new FiringPolicy("S1S5", Direction.South);
        policiesList.add(S1S5);
        FiringPolicy S10S6 = new FiringPolicy("S10S6", Direction.North);
        policiesList.add(S10S6);
        FiringPolicy S9S6 = new FiringPolicy("S9S6", Direction.North);
        policiesList.add(S9S6);
        FiringPolicy S5S9 = new FiringPolicy("S5S9", Direction.South);
        policiesList.add(S5S9);
        FiringPolicy S5S8 = new FiringPolicy("S5S8", Direction.South);
        policiesList.add(S5S8);
    }

    ArrayList<FiringPolicy> getPoliciesList()
    {
        return policiesList;
    }

    void create_pointMachine(int machine_id, int prioritized_section_1, int prioritized_section_2, int second_section_1, int second_section_2)
    {
        PointMachine pointMachine = new PointMachine(machine_id);
        String prioritized_policy_name = "S"+prioritized_section_1+"S"+prioritized_section_2;
        String second_policy_name = "S"+second_section_1+"S"+second_section_2;

        // index 0 for prioritized policy
        for (int i = 0; i < policiesList.size(); i++)
        {
            if (policiesList.get(i).getPolicy_name().equals(prioritized_policy_name))
            {
                pointMachine.add_policy(policiesList.get(i));
            }
        }

        // index 1 for second policy
        for (int i = 0; i < policiesList.size(); i++)
        {
            if (policiesList.get(i).getPolicy_name().equals(second_policy_name))
            {
                pointMachine.add_policy(policiesList.get(i));
            }
        }

        pointMachine.default_enabled_policy(0,true);
        pointMachine.default_enabled_policy(1,false);

        pointMachineList.add(pointMachine);
    }

    void init_pointMachines()
    {
        create_pointMachine(1,3,7,3,4);
        create_pointMachine(2,6,2,3,4);
        create_pointMachine(3,1,5,3,4);
        create_pointMachine(4,9,6,10,6);
        create_pointMachine(5,9,6,5,9);
        create_pointMachine(6,9,6,5,8);
    }

    ArrayList<PointMachine> getPointMachineList()
    {
        return pointMachineList;
    }

    void pointMachine_changeEnabled(int machine_id)
    {
        PointMachine target_machine = pointMachineList.get(machine_id - 1);
        ArrayList<FiringPolicy> tmp_policyList = target_machine.getControlled_policies();

        for (int i = 0; i < tmp_policyList.size(); i++)
        {
            if (tmp_policyList.get(i).getEnabled() == true)
            {
                tmp_policyList.get(i).setEnabled(false);
            } else
            {
                tmp_policyList.get(i).setEnabled(true);
            }
        }
    }

    // function to find the index of a train in present_train_list
    int getTrainPos(String name, ArrayList<Train> trainList)
    {
        int pos;
        for (int i = 0; i < trainList.size(); i++)
        {
            if (trainList.get(i).getTrainName().equals(name))
            {
                pos = i;
                return pos;
            }
        }
        return -1;
    }

    void reset_to_default(int m_id)
    {
        PointMachine pointMachine = pointMachineList.get(m_id - 1);
        ArrayList<FiringPolicy> policies = pointMachine.getControlled_policies();

        if (policies.get(0).getEnabled() == false)
        {
            pointMachine_changeEnabled(m_id);
        }

    }

    void update_PointMachine(ArrayList<Section> sectionsList, ArrayList<Train> trainList)
    {
        for (PointMachine pointMachine: pointMachineList)
        {
            boolean current_second_policy_status = pointMachine.getControlled_policies().get(1).getEnabled();

            // update instruction for point machine 1
            if (pointMachine.getMachine_id() == 1)
            {
                if ((sectionsList.get(2).getOccupyingTrain_name().equals("") && (sectionsList.get(6).getOccupyingTrain_name().equals(""))) && current_second_policy_status == false)
                {
                    pointMachine_changeEnabled(1);

                } else if (sectionsList.get(2).getOccupyingTrain_name().equals("") && !sectionsList.get(6).getOccupyingTrain_name().equals("") && current_second_policy_status == false)
                {
                    int sec7TrainPos = getTrainPos(sectionsList.get(6).getOccupyingTrain_name(), trainList);
                    Train sec7Train = trainList.get(sec7TrainPos);

                    if (sec7Train.getTrain_direction() == Direction.South)
                    {
                        pointMachine_changeEnabled(1);
                    }
                } else if (!sectionsList.get(2).getOccupyingTrain_name().equals("") || !sectionsList.get(6).getOccupyingTrain_name().equals(""))
                {
                    reset_to_default(1);
                }
            }

            // update instruction for point machine 2
            if (pointMachine.getMachine_id() == 2)
            {
                if (sectionsList.get(5).getOccupyingTrain_name().equals("") && current_second_policy_status == false)
                {
                    pointMachine_changeEnabled(2);

                } else {
                    reset_to_default(2);
                }
            }

            // update instruction for point machine 3
            if (pointMachine.getMachine_id() == 3)
            {
                if (sectionsList.get(0).getOccupyingTrain_name().equals("") && current_second_policy_status == false)
                {
                    pointMachine_changeEnabled(3);
                } else {
                    reset_to_default(3);
                }
            }

            // update instruction for point machine 4
            if (pointMachine.getMachine_id() == 4)
            {
                if (sectionsList.get(5).getOccupyingTrain_name().equals("") && (sectionsList.get(8).getOccupyingTrain_name().equals("")) && current_second_policy_status == false)
                {
                    pointMachine_changeEnabled(4);

                } else if (sectionsList.get(5).getOccupyingTrain_name().equals("") && !sectionsList.get(8).getOccupyingTrain_name().equals("") && current_second_policy_status == false)
                {
                    int sec9TrainPos = getTrainPos(sectionsList.get(8).getOccupyingTrain_name(), trainList);
                    Train sec9Train = trainList.get(sec9TrainPos);

                    if (sec9Train.getTrain_direction() == Direction.South)
                    {
                        pointMachine_changeEnabled(4);
                    }

                } else if (!sectionsList.get(5).getOccupyingTrain_name().equals("") || !sectionsList.get(8).getOccupyingTrain_name().equals(""))
                {
                    reset_to_default(4);
                }
            }

            // update instruction for point machine 5
            if (pointMachine.getMachine_id() == 5)
            {
                if (sectionsList.get(8).getOccupyingTrain_name().equals("") && current_second_policy_status == false)
                {
                    pointMachine_changeEnabled(5);

                } else {
                    reset_to_default(5);
                }
            }

            // update instruction for point machine 6
            if (pointMachine.getMachine_id() == 6)
            {
                if (sectionsList.get(8).getOccupyingTrain_name().equals("") && current_second_policy_status == false)
                {
                    pointMachine_changeEnabled(6);

                } else if (!sectionsList.get(8).getOccupyingTrain_name().equals(""))
                {
                    int sec9TrainPos2 = getTrainPos(sectionsList.get(8).getOccupyingTrain_name(), trainList);
                    Train sec9Train2 = trainList.get(sec9TrainPos2);

                    if (sec9Train2.getTrain_direction() == Direction.South)
                    {
                        pointMachine_changeEnabled(6);

                    } else if (sec9Train2.getTrain_direction() == Direction.North)
                    {
                        reset_to_default(6);
                    }
                }
            }
        }
    }
}
