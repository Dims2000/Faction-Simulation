import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main
{
    private static List<Student> students = new ArrayList<>();
    private static Locations locations = new Locations();
    private static List<Student> realOrder = new ArrayList<>();
    private static Scanner input = new Scanner(System.in);
    private static List<Student> followList = new ArrayList<>();
    private static List<Faction> factionList = new ArrayList<>();

    public static void main(String[] args)
    {
        System.out.print("Number of Students: ");
        int num = input.nextInt();
        System.out.println();

        int response;

        while (true)
        {
            System.out.print("Create Custom Student (Yes = 1 / No = 2): ");
            response = input.nextInt();

            if (response == 1)
            {
                response = 3;

                while (response != 1 && response != 2)
                {
                    System.out.print("\nMale or Female (Male = 1 / Female = 2): ");
                    response = input.nextInt();
                }

                boolean male = (response == 1);

                input.nextLine();
                System.out.print("First Name: ");
                String firstName = input.nextLine();

                System.out.print("Last Name: ");
                String lastName = input.nextLine();

                int strength = 0;

                while (strength <= 0 || strength > 11)
                {
                    System.out.print("\nStrength (1-10): ");
                    strength = input.nextInt();
                }

                int agility = 0;

                while (agility <= 0 || agility > 11)
                {
                    System.out.print("Agility (1-10): ");
                    agility = input.nextInt();
                }

                int creativity = 0;

                while (creativity <= 0 || creativity > 11)
                {
                    System.out.print("Creativity (1-10): ");
                    creativity = input.nextInt();
                }

                int goodness = 0;

                while (goodness <= 0 || goodness > 11)
                {
                    System.out.print("Goodness (1-10): ");
                    goodness = input.nextInt();
                }

                int selfishness = 0;

                while (selfishness <= 0 || selfishness > 11)
                {
                    System.out.print("Selfishness (1-10): ");
                    selfishness = input.nextInt();
                }

                num--;
                Student s1 = new Student(firstName, lastName, male, strength, agility, creativity, goodness, selfishness);
                s1.assignLocation(locations.addToRandomRoom(s1));
                students.add(s1);
                followList.add(s1);
                System.out.println();
            }
            else
            {
                System.out.println();
                break;
            }
        }

        for (int i = 0; i < num; i++)
        {
            Student s1 = new Student();
            s1.assignLocation(locations.addToRandomRoom(s1));
            students.add(s1);
            System.out.println((i+1) + ". " + s1.getFullName());
            pause(100);
        }

        System.out.println((num + 1) + ". Everyone");
        System.out.println("\nWho do you want to follow:");

        int correctionNum = followList.size();

        while (!input.equals(0))
        {
            System.out.print("\n>");
            num = input.nextInt();

            if (num <= 0)
                break;

            if (num > students.size())
            {
                followList = new ArrayList<>();
                break;
            }

            followList.add(students.get(num - 1 + correctionNum));
            System.out.println("\n" + students.get(num - 1 + correctionNum).getFirstName() + "'s stats will be tracked");
        }

        if (followList.size() == 0)
            followList.addAll(students);

        System.out.print("\nNumber of Days: ");
        num = input.nextInt();

        int originalFood = students.size() * 14;
        input.nextLine();

        for (int days = 0; days < num; days++)
        {
            List<Student> tempOrder = new ArrayList<>(students);
            realOrder = new ArrayList<>();

            for (int i = 0; i < students.size(); i++)
            {
                int strongestLevel = 0;
                Student strongestStudent = null;

                for (Student student : tempOrder)
                {
                    if (student.getPower() > strongestLevel)
                    {
                        strongestLevel = student.getPower();
                        strongestStudent = student;
                    }
                }

                realOrder.add(strongestStudent);
                tempOrder.remove(strongestStudent);
            }

            System.out.println("\nUpdates:");
            System.out.println("\nStudents Alive: " + realOrder.size());
            System.out.println("Next Supply Drop: " + (originalFood == 0 ? "Never" : (7 - ((days+ 1) % 7)) + " days"));
            pause(250);

            System.out.println("\nDay " + (days + 1) + " Log:\n");

            if (((days + 1) % 7 == 0 || days == 0) && originalFood > 0)
            {
                originalFood *= 0.5;
                locations.supplyDrop(originalFood);
                System.out.println("The supply drop arrived with " + originalFood + " food\n");
                pause(250);
            }

            for (int i = 0; i < realOrder.size(); i++)
            {
                if (followList.contains(realOrder.get(i)))
                {
                    System.out.println();
                    director(realOrder.get(i), true);
                    System.out.println();
                    pause(350);
                }
                else
                    director(realOrder.get(i), false);
            }

            for (int i = 0; i < factionList.size(); i++)
            {
                if (factionList.get(i).getMembers().size() == 0)
                {
                    factionList.remove(i);
                    i--;
                }
            }

            System.out.println("-----------------------------------------------------------");

            String line = "Hello World";

            while (!line.equals(""))
            {
                System.out.print("\n>");
                line = input.nextLine();

                if (line.contains("unfollow"))
                {
                    String name = line.substring(line.indexOf(" ") + 1);

                    for (Student student : students)
                    {
                        if (student.getFullName().equals(name))
                        {
                            followList.remove(student);
                            System.out.println("\n" + student.getFirstName() + "'s stats will no longer be tracked");
                            break;
                        }
                    }
                }
                else if (line.contains("follow"))
                {
                    String name = line.substring(line.indexOf(" ") + 1);

                    for (Student student : students)
                    {
                        if (student.getFullName().equals(name))
                        {
                            followList.add(student);
                            System.out.println("\n" + student.getFirstName() + "'s stats will now be tracked");
                            break;
                        }
                    }
                }

                if (line.toLowerCase().contains("stats"))
                {
                    for (Student student : students)
                    {
                        if (followList.indexOf(student) != -1)
                        {
                            System.out.println();
                            student.printStudent();
                            pause(500);
                        }
                    }
                }

                if (line.toLowerCase().contains("map"))
                {
                    System.out.println();
                    locations.viewRooms();
                }

                if (line.toLowerCase().equals("order"))
                {
                    for (int i = 0; i < realOrder.size(); i++)
                    {
                        System.out.println((i + 1) + ". " + realOrder.get(i).getFullName() + ": " + realOrder.get(i).getPower());
                    }
                }

                if (line.toLowerCase().contains("faction"))
                {
                    for (Faction faction : factionList)
                    {
                        System.out.println("\n" + faction.getName() + ":");
                        System.out.println("\nType: " + faction.getLeader().getType());
                        System.out.println("Leader: " + faction.getLeader().getFullName());

                        String membersVis = "";

                        if (faction.getMembers().size() == 1)
                            membersVis = faction.getMembers().get(0).getFullName();
                        else if (faction.getMembers().size() == 2)
                            membersVis = faction.getMembers().get(0).getFullName() + " and " + faction.getMembers().get(1).getFullName();
                        else
                        {
                            for (int i = 0; i < faction.getMembers().size(); i++)
                            {
                                if (i == faction.getMembers().size() - 1)
                                {
                                    membersVis += "and " + faction.getMembers().get(i).getFullName();
                                    break;
                                }

                                membersVis += faction.getMembers().get(i).getFullName() + ", ";
                            }
                        }

                        System.out.println("Members: " + membersVis);

                        String exiledVis = "";

                        if (faction.getMemberBlackList().size() == 1)
                            exiledVis = faction.getMemberBlackList().get(0).getFullName();
                        else if (faction.getMemberBlackList().size() == 2)
                            exiledVis = faction.getMemberBlackList().get(0).getFullName() + " and " + faction.getMemberBlackList().get(1).getFullName();
                        else
                        {
                            for (int i = 0; i < faction.getMemberBlackList().size(); i++)
                            {
                                if (i == faction.getMemberBlackList().size() - 1)
                                {
                                    exiledVis += "and " + faction.getMemberBlackList().get(i).getFullName();
                                    break;
                                }

                                exiledVis += faction.getMemberBlackList().get(i).getFullName() + ", ";
                            }
                        }

                        System.out.println("Exiled Members: " + (exiledVis.equals("") ? "None" : exiledVis));
                        System.out.println("Faction Food: " + faction.getFactionFood());
                        System.out.println("Headquarters: " + (faction.securedRoom() ? faction.getHeadquarters().getName() : "Variable"));
                        System.out.println("Power: " + faction.getFactionPower());
                        System.out.println("--------------------------------------------------");
                    }
                }
            }
        }
    }

    private static void pause(int milliseconds)
    {
        try
        {
            Thread.sleep(milliseconds);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private static void director(Student student, boolean printLog)
    {
        if ((student.getHungerVis().equals("Well-Fed") || student.getHungerVis().equals("Nourished")) && student.getHealthNum() < 10)
            student.gainHealth(1);

        if ((student.getFood() == 0 || student.getHealthNum() > 6) && student.isHiding())
        {
            student.getLocation().removeHider(student);

            if (student.getLocation().getOccupancy() >= 4)
                move(student);

            if (printLog)
                System.out.println(student.getFullName() + " emerged from hiding in the " + student.getLocation().getName());

            if (!student.getHealthVis().equals("Crippled"))
                student.setMobility(true);
        }

        if (student.hasTrait("Fat") && (Math.random() * 10) < 2)
            student.setMobility(false);

        if (student.getFood() < student.getSelfishness())
        {
            if (student.getLocation().getFoodAvailable() == 0 && student.getMobility() && locations.foodInCourtyard() != 0)
                student.assignLocation(locations.goToSpecificRoom(student, 0));
            else
                move(student);

            if (student.getLocation().getFoodAvailable() > 0)
                student.takeFood();
        }

        student.updateStates(printLog);

        boolean needToEat = true;

        if (student.getSelfishness() > (int)(Math.random() * 12) + 1 && student.getMobility())
        {
            String trait = (student.getTraits().size() > 0 ? student.getTraits().get((int)(Math.random() * student.getTraits().size())) : " ");
            student.updateStates(printLog);

            switch (trait)
            {
                case "Popular":
                    if (!student.getType().equals("Outcast"))
                    {
                        befriend(student);
                        befriend(student);
                    }
                    break;
                case "Shy":
                    if (student.getMobility())
                        student.assignLocation(locations.goToEmptyRoom(student));
                    break;
                case "Altruistic":
                    needToEat = false;
                    break;
                case "Creative":
                    if (student.getMobility())
                        move(student);
                    break;
                case "Kind":
                    for (Student partner : student.getLocation().getStudentsInRoom())
                    {
                        if (!partner.getFullName().equals(student.getFullName()) && partner.getHealthVis().equals("Crippled"))
                        {
                            partner.setMobility(true);
                            partner.setHealthVis("Patched");

                            if (printLog || followList.contains(partner))
                                System.out.println(student.getFullName() + " showed an act of kindness and patched up " + partner.getFullName());

                            break;
                        }
                    }
                    break;
                case "Smart":
                    if (student.getFood() < student.getSelfishness())
                    {
                        student.assignLocation(locations.goToRoomWithFood(student));

                        if (student.getLocation().getFoodAvailable() > 0)
                            student.takeFood();
                    }
                    break;
                case "Fat":
                    if (student.getFood() < student.getSelfishness())
                        student.setFood(student.getFood() + 1);
                    break;
                case "Ugly":
                    if (!student.isHiding())
                    {
                        if (printLog)
                            System.out.println(student.getFullName() + " went into hiding in the " + student.getLocation().getName());

                        student.getLocation().addHider(student);
                        student.setMobility(false);
                    }
                    break;
                case "Handsome":
                    if (student.isRomanced() && (int)(Math.random() * 10) + 1 == 1)
                        student.increaseStat();
                    break;
                case "Pretty":
                    if (student.isRomanced() && (int)(Math.random() * 10) + 1 == 1)
                        student.increaseStat();
                    break;
                case "Evil":
                    if (student.isInFaction() && (int)(Math.random() * 10) + 1 <= 2 && student.getHungerVis().equals("Starving"))
                    {
                        if (printLog)
                            System.out.println(student.getFullName() + " betrayed the " + student.getFaction().getName() + " by killing " + student.getFaction().getLeader().getFullName());

                        die(student.getFaction().getLeader());

                        if (student.getFaction().getFactionFood() > 0)
                        {
                            int stolenFood = student.getFaction().getFactionFood();
                            student.setFood(student.getFood() + stolenFood);

                            if (student.getFood() > 10)
                            {
                                student.getLocation().setFoodAvailable(student.getLocation().getFoodAvailable() + (10 - student.getFood()));
                                student.setFood(10);
                            }
                        }
                        else
                            student.setFood(10);

                        student.exile();
                    }
            }
        }
        else if (student.getMobility() || (student.getHealthVis().equals("Crippled") && !student.isHiding()))
        {
            if (!student.getType().equals("Outcast"))
            {
                if (!student.isInFaction() && student.getFriends().size() >= 2)
                {
                    Faction faction = new Faction(student);
                    student.setFaction(faction);
                    factionList.add(faction);

                    if (!printLog)
                    {
                        for (Student member : faction.getMembers())
                        {
                            if (followList.contains(member))
                                System.out.println(member.getFullName() + " became a founding member of " + faction.getName());
                        }
                    }
                    else
                    {
                        if (faction.getMembers().size() == 1)
                            System.out.println(student.getFullName() + " founded the " + faction.getName() + " as a single-member " + student.getType().toLowerCase() + " faction");
                        else
                            System.out.println(faction.getName() + " emerged as a new " + student.getType().toLowerCase() + " faction with " + student.getFullName() + " as its leader");
                    }
                }
                else
                    befriend(student);

                if (student.isInFaction()  && student.equals(student.getFaction().getLeader()) && (int)(Math.random() * 40) + 1 <= 10 - student.getGoodness() && !student.getTraits().contains("Kind"))
                {
                    for (Student member : student.getFaction().getMembers())
                    {
                        if (!member.equals(student) && !(student.isRomanced() && student.getRomancePartner().equals(member)) && !student.getFriends().contains(member) &&
                                (member.getTraits().contains("Evil") || (!member.getTraits().contains("Kind") && member.getSelfishness() > 6)))
                        {
                            student.getFaction().blacklistMember(member);
                            member.guaranteeAddToBlacklist(student);

                            if (student.getFriends().contains(member))
                            {
                                student.removeFriend(member);
                                student.guaranteeAddToBlacklist(member);
                                member.removeFriend(student);
                            }

                            if (printLog || followList.contains(member))
                                System.out.println(student.getFullName() + " exiled " + member.getFullName() + " from the " + student.getFaction().getName());

                            break;
                        }
                    }
                }

                if (student.isInFaction() && !student.getFaction().getLeader().equals(student) && !student.getTraits().contains("Kind") &&
                        student.getFaction().getLeader().getGoodness() <= (int)(Math.random() * 10) && student.getFriendBlackList().contains(student.getFaction().getLeader()) &&
                        (student.getFriends().size() > student.getFaction().getLeader().getFriends().size() || student.getPower() > student.getFaction().getLeader().getPower()))
                {
                    student.getFaction().blacklistMember(student.getFaction().getLeader());
                    student.guaranteeAddToBlacklist(student.getFaction().getLeader());
                    student.getFaction().getLeader().guaranteeAddToBlacklist(student);

                    if (printLog || followList.contains(student.getFaction().getLeader()))
                        System.out.println(student.getFullName() + " overthrew and exiled " + student.getFaction().getLeader().getFullName() + ". " + student.getFirstName() + " is now the leader of the " + student.getFaction().getName());

                    student.getFaction().setFactionLeader(student);
                }

                if (student.isInFaction() && student.equals(student.getFaction().getLeader()) && !student.getFaction().securedRoom() && (int)(Math.random() * 10) + 1 <= 2 && student.getFaction().getMembers().size() >= 3)
                {
                    switch (student.getType())
                    {
                        case "Jock":
                            student.assignLocation(locations.goToSpecificRoom(student, 2));

                            if (student.getLocation().getName().equals("Gym"))
                            {
                                if (!student.getLocation().isSecured())
                                {
                                    if (printLog)
                                        System.out.println(student.getFaction().getName() + " were able to secure the Gym");

                                    student.getFaction().secureRoom(student.getLocation());
                                    student.getLocation().setControllingFaction(student.getFaction());
                                    break;
                                }

                                Student opponent = student.getLocation().getControllingFaction().getLeader();

                                if (opponent.isHiding())
                                    break;

                                if (!student.getFaction().securedRoom() && !opponent.getFaction().getMemberBlackList().contains(student) && !opponent.getFriendBlackList().contains(student) && (int)(Math.random() * 40) + 1 <= 10 - student.getGoodness() &&
                                        !student.getFriends().contains(opponent) && !student.getFaction().getMemberBlackList().contains(opponent) && (student.getFaction().getFactionPower() - opponent.getFaction().getFactionPower()) >= -20)
                                {
                                    if (printLog || followList.contains(opponent))
                                        System.out.println(student.getFullName() + " challenged " + opponent.getFullName() +
                                                " of to a solo fight for leadership of the " + opponent.getFaction().getName());

                                    fight(student, opponent, false);

                                    if (students.contains(student) && factionList.contains(opponent.getFaction()))
                                    {
                                        opponent.getFaction().removeHeadquarters();
                                        student.getLocation().setControllingFaction(student.getFaction());
                                        student.getFaction().secureRoom(student.getLocation());

                                        if (printLog)
                                            System.out.println(opponent.getFaction().getName() + " relinquished their hold on the Gym to the " + student.getFaction().getName());
                                    }
                                    else if (!factionList.contains(opponent.getFaction()))
                                    {
                                        if (printLog)
                                            System.out.println(student.getFaction().getName() + " were able to secure the Gym");

                                        student.getFaction().secureRoom(student.getLocation());
                                        student.getLocation().setControllingFaction(student.getFaction());
                                    }
                                }
                            }
                            break;
                        case "Gamer":
                            student.assignLocation(locations.goToSpecificRoom(student, 9));

                            if (student.getLocation().getName().equals("Computer Lab"))
                            {
                                if (!student.getLocation().isSecured())
                                {
                                    if (printLog)
                                        System.out.println(student.getFaction().getName() + " were able to secure the Computer Lab");

                                    student.getFaction().secureRoom(student.getLocation());
                                    student.getLocation().setControllingFaction(student.getFaction());
                                }

                                Student opponent = student.getLocation().getControllingFaction().getLeader();

                                if (opponent.isHiding())
                                    break;

                                if (!opponent.getFaction().getMemberBlackList().contains(student) && !opponent.getFriendBlackList().contains(student) &&
                                        !student.getFriends().contains(opponent) && !student.getFaction().getMemberBlackList().contains(opponent) && (student.getFaction().getFactionPower() - opponent.getFaction().getFactionPower()) >= -20)
                                {
                                    int studentFactionVote = 0;
                                    int opponentFactionVote = 0;

                                    if (printLog || followList.contains(opponent))
                                        System.out.print(student.getFaction().getName() + " and " + opponent.getFaction().getName() + " voted for who should have ownership of the Computer Lab: ");

                                    while (studentFactionVote == opponentFactionVote)
                                    {
                                        studentFactionVote = 0;
                                        opponentFactionVote = 0;

                                        for (Student voter : opponent.getFaction().getMembers())
                                        {
                                            int differences = Math.abs(student.getBaseStrength() - voter.getBaseStrength()) + Math.abs(student.getAgility() - voter.getAgility())
                                                    + Math.abs(student.getCreativity() - voter.getCreativity()) + Math.abs(student.getGoodness() - voter.getGoodness())
                                                    + Math.abs(student.getSelfishness() - voter.getSelfishness());

                                            if ((int)(Math.random() * 50) + 1 > differences)
                                                studentFactionVote++;
                                            else
                                                opponentFactionVote++;
                                        }
                                    }

                                    if (studentFactionVote > opponentFactionVote)
                                    {
                                        if (printLog || followList.contains(opponent))
                                            System.out.println(student.getFaction().getName() + " won the vote");

                                        opponent.getFaction().removeHeadquarters();
                                        student.getLocation().setControllingFaction(student.getFaction());
                                        student.getFaction().secureRoom(student.getLocation());
                                    }
                                    else
                                    {
                                        if (printLog || followList.contains(opponent))
                                            System.out.println(opponent.getFaction().getName() + " won the vote");

                                        student.guaranteeAddToBlacklist(opponent);
                                        opponent.guaranteeAddToBlacklist(student);
                                    }
                                }
                            }
                            break;
                            /*
                        case "Nerd":
                            student.assignLocation(locations.goToSpecificRoom(student, 6));

                            if (student.getLocation().getName().equals("Chemistry Classroom") && !student.getLocation().isSecured())
                            {
                                if (printLog)
                                    System.out.println(student.getFaction().getName() + " were able to secure the Chemistry Classroom");

                                student.getFaction().secureRoom(student.getLocation());
                                student.getLocation().setControllingFaction(student.getFaction());
                            }
                            break;
                        case "Artist":
                            student.assignLocation(locations.goToSpecificRoom(student, 1));

                            if (student.getLocation().getName().equals("Auditorium") && !student.getLocation().isSecured())
                            {
                                if (printLog)
                                    System.out.println(student.getFaction().getName() + " were able to secure the Auditorium");

                                student.getFaction().secureRoom(student.getLocation());
                                student.getLocation().setControllingFaction(student.getFaction());
                            }
                            break;
                            */
                    }
                }

                switch (student.getType())
                {
                    case "Jock":
                        break;
                    case "Gamer":
                        break;
                    case "Nerd":
                        break;
                    case "Artist":
                        break;
                }
            }
        }

        if (student.getFood() == 0 && student.getFriends().size() > 0 && !student.isHiding() && ((student.getStrength() >= 7) || (student.getStrength() >= 4 &&
                student.getHungerNum() % 2 == 0) || (student.getStrength() <= 3 && student.getHungerNum() % 3 == 0) || student.getHungerNum() > 3))
        {
            for (Student friend : student.getFriends())
            {
                if (friend.getFood() > friend.getSelfishness() && !friend.isHiding())
                {
                    transferFood(student, friend, (friend.getFood() - friend.getSelfishness()));

                    if (printLog && followList.contains(friend))
                        System.out.println(friend.getFullName() + " gave some food to " + (friend.isMale() ? "his" : "her") + " friend, " + student.getFullName());

                    break;
                }
            }
        }

        if (student.getFood() < student.getSelfishness() && !student.isHiding())
        {
            for (Student giver : student.getLocation().getStudentsInRoom())
            {
                if (!giver.getFullName().equals(student.getFullName()) && giver.getFood() > giver.getSelfishness() && !giver.getTraits().contains("Evil"))
                {
                    if (giver.getGoodness() >= (int)(Math.random() * 10) + 1 && !giver.getFriendBlackList().contains(student))
                    {
                        transferFood(student, giver, (giver.getFood() - giver.getSelfishness()));

                        /*
                        if (printLog || followList.contains(giver))
                        {
                            if (giver.getTraits().contains("Altruistic"))
                                System.out.println(giver.getFullName() + " helped " + student.getFullName() + " by giving " + (student.isMale() ? "him" : "her") + " some food");
                            else
                                System.out.println(student.getFullName() + " asked " + giver.getFullName() + " for some food. " + giver.getFirstName() + " was happy to give some");
                        }
                        */
                    }
                    else
                    {
                        //if (printLog || followList.contains(giver))
                        //System.out.println(student.getFullName() + " asked " + giver.getFullName() + " for some food, but " + giver.getFirstName() + " refused");

                        student.addToBlacklist(giver, (printLog || followList.contains(giver)));
                    }

                    break;
                }
            }
        }

        if (student.isInFaction() && student.getHealthNum() % 2 == 0 && (student.equals(student.getFaction().getLeader()) ||
                student.getFaction().getLeader().getGoodness() > (int)(Math.random() * 10)) && student.getFaction().getFactionFood() > 0 && student.getFood() == 0)
        {
            student.setFood(student.getFood() + 1);
            student.getFaction().removeFactionFood(1);
        }

        if (needToEat)
            student.eat();

        if (student.getHealthNum() <= 0)
        {
            if (student.getTraits().contains("Fat"))
                student.getLocation().setFoodAvailable(20);
            else
                student.getLocation().setFoodAvailable(10);

            die(student);
            if (printLog)
                System.out.println(student.getFullName() + " died of starvation");
            return;
        }

        student.updateStates(printLog);

        if (student.isInFaction() && student.getFood() > student.getSelfishness())
        {
            student.getFaction().addFactionFood(1);
            student.setFood(student.getFood() - 1);
        }

        if (student.getMobility())
        {
            if (student.getAggression().equals("Agitated") || student.getAggression().equals("Aggressive"))
            {
                if (student.getLocation().getOccupancy() >= 2)
                {
                    Student mostFood = student.getLocation().getStudentWithMostFood(student);

                    if (student.getAggression().equals("Agitated") && ((mostFood.getFood() >= 7 && !student.getTraits().contains("Kind")) ||
                            (student.getHungerVis().equals("Starving") && mostFood.getFood() > 0)) &&
                            !mostFood.getFullName().equals(student.getFullName()) &&
                            (student.getFriends().size() == 0 || !student.getFriends().contains(mostFood)) && !(student.isInFaction() && mostFood.isInFaction() && student.getFaction().equals(mostFood.getFaction())))
                        fight(student, mostFood, true);
                    else if (student.getAggression().equals("Aggressive") && !mostFood.getFullName().equals(student.getFullName()) && (!student.hasTrait("Kind") || mostFood.getFood() > 0) &&
                            (student.getFriends().size() == 0 || !student.getFriends().contains(mostFood)) && !(student.isInFaction() && mostFood.isInFaction() && student.getFaction().equals(mostFood.getFaction())))
                        fight(student, mostFood, true);
                }
            }
        }

        if (student.getHealthNum() <= 0)
            return;

        if (student.getFriends().size() > 0 && (int)(Math.random() * 10) + 1 == 1)
        {
            Student randomFriend = student.getFriends().get((int)(Math.random() * student.getFriends().size()));

            int differences = Math.abs(student.getBaseStrength() - randomFriend.getBaseStrength()) + Math.abs(student.getAgility() - randomFriend.getAgility())
                    + Math.abs(student.getCreativity() - randomFriend.getCreativity()) + Math.abs(student.getGoodness() - randomFriend.getGoodness())
                    + Math.abs(student.getSelfishness() - randomFriend.getSelfishness());

            if (!(student.isRomanced() && student.getRomancePartner().equals(randomFriend)) && (int)(Math.random() * 50) + 1 < differences)
            {
                student.removeFriend(randomFriend);
                randomFriend.removeFriend(student);
                student.guaranteeAddToBlacklist(randomFriend);
                randomFriend.guaranteeAddToBlacklist(student);

                if (printLog || followList.contains(randomFriend))
                    System.out.println(student.getFullName() + " and " + randomFriend.getFullName() + " had a falling out and are no longer friends");
            }
            else if (!student.isRomanced() && !randomFriend.isRomanced() && ((student.isMale() && !randomFriend.isMale()) ||
                    (!student.isMale() && randomFriend.isMale())))
            {
                if (!student.getTraits().contains("Popular") || (randomFriend.getTraits().contains("Popular") ||
                        randomFriend.getTraits().contains("Pretty") || randomFriend.getTraits().contains("Handsome")))
                    romance(student, randomFriend);
            }
        }

        if (student.getHealthNum() <= 6 && student.getFood() > 0 && !student.isHiding())
        {
            if (printLog)
                System.out.println(student.getFullName() + " went into hiding in the " + student.getLocation().getName());

            student.getLocation().addHider(student);
            student.setMobility(false);
        }

        if (!student.getHealthVis().equals("Crippled") && student.hasTrait("Fat") && !student.getLocation().getHiders().contains(student))
            student.setMobility(true);

        student.updateStates(printLog);

        if (student.getMobility())
            move(student);
    }

    private static void die(Student student)
    {
        locations.removeFromMap(student);
        students.remove(student);
        realOrder.remove(student);
        student.unRomance();

        if (student.getFriends().size() > 0)
        {
            for (Student friend : student.getFriends())
            {
                friend.removeFriend(student);
            }
        }

        for (Student possibleEnemy : students)
        {
            if (possibleEnemy.getFriendBlackList().contains(student))
                possibleEnemy.removeFromBlacklist(student);
        }

        for (Faction faction : factionList)
        {
            if (faction.getMemberBlackList().contains(student))
                faction.removeFromBlacklist(student);
        }

        if (student.isInFaction())
        {
            student.getFaction().removeMember(student);

            if (student.getFaction().getLeader().getFullName().equals(student.getFullName()) && student.getFaction().getMembers().size() > 0)
            {
                Student nextLeader = student.getFaction().getMembers().get(0);

                if (student.getFaction().getMembers().size() > 1)
                {
                    for (Student member : student.getFaction().getMembers())
                    {
                        if (member.getPower() > nextLeader.getPower())
                            nextLeader = member;
                    }
                }

                nextLeader.getFaction().setFactionLeader(nextLeader);

                if (followList.contains(nextLeader) || followList.contains(student))
                    System.out.println(nextLeader.getFullName() + " became the next leader of " + nextLeader.getFaction().getName());
            }
            else if (student.getFaction().getMembers().size() == 0)
            {
                factionList.remove(student.getFaction());

                if (student.getFaction().securedRoom())
                {
                    student.getFaction().getHeadquarters().removeControllingFaction();
                    student.getFaction().removeHeadquarters();
                }

                if (student.getFaction().getFactionFood() > 0)
                    student.getLocation().setFoodAvailable(student.getLocation().getFoodAvailable() + student.getFaction().getFactionFood());

                if (followList.contains(student))
                    System.out.println(student.getFaction().getName() + " no longer exist");
            }
        }

        followList.remove(student);
    }

    private static void befriend(Student student)
    {
        if (student.getLocation().getOccupancy() > 1)
        {
            for (Student partner : student.getLocation().getStudentsInRoom())
            {
                if (!student.getFullName().equals(partner.getFullName()) && !student.getFriends().contains(partner) &&
                        partner.getType().equals(student.getType()) && !partner.getType().equals("Outcast") &&
                        (!partner.getFriendBlackList().contains(student) && !student.getFriendBlackList().contains(partner)))
                {
                    if (student.isInFaction() && student.getFaction().getLeader().getFullName().equals(student.getFullName()) &&
                            partner.isInFaction() && partner.getFaction().getLeader().getFullName().equals(partner.getFullName()) )
                    {
                        int differences = Math.abs(student.getBaseStrength() - partner.getBaseStrength()) + Math.abs(student.getAgility() - partner.getAgility())
                                + Math.abs(student.getCreativity() - partner.getCreativity()) + Math.abs(student.getGoodness() - partner.getGoodness())
                                + Math.abs(student.getSelfishness() - partner.getSelfishness());

                        if ((int)(Math.random() * 50) + 1 > differences)
                        {
                            student.addFriend(partner);
                            partner.addFriend(student);

                            if (student.getFaction().getFactionPower() >= partner.getFaction().getFactionPower())
                            {
                                factionList.remove(partner.getFaction());
                                student.getFaction().addFactionFood(partner.getFaction().getFactionFood());

                                if (followList.contains(student) || followList.contains(partner))
                                    System.out.println(student.getFullName() + " of " + student.getFaction().getName() + " and " + partner.getFullName() +
                                            " of " + partner.getFaction().getName() + " became friends and joined their forces under the name " + student.getFaction().getName());

                                for (Student member : partner.getFaction().getMembers())
                                {
                                    if (!student.getFaction().getMemberBlackList().contains(member))
                                    {
                                        member.setFaction(student.getFaction());
                                        student.getFaction().addMember(member);
                                    }
                                    else
                                        member.exile();
                                }
                            }
                            else
                            {
                                factionList.remove(student.getFaction());
                                partner.getFaction().addFactionFood(student.getFaction().getFactionFood());

                                if (followList.contains(student) || followList.contains(partner))
                                    System.out.println(partner.getFullName() + " of " + partner.getFaction().getName() + " and " + student.getFullName() +
                                            " of " + student.getFaction().getName() + " became friends and joined their forces under the name " + partner.getFaction().getName());

                                for (Student member : student.getFaction().getMembers())
                                {
                                    if (!partner.getFaction().getMemberBlackList().contains(member))
                                    {
                                        member.setFaction(partner.getFaction());
                                        partner.getFaction().addMember(member);
                                    }
                                    else
                                        member.exile();
                                }
                            }
                        }
                        else
                        {
                            student.addToBlacklist(partner, followList.contains(student));
                            partner.addToBlacklist(student, followList.contains(partner));
                        }

                        return;
                    }
                    else if (!partner.isInFaction() && (!student.isInFaction() || !student.getFaction().getMemberBlackList().contains(partner)))
                    {
                        student.addFriend(partner);
                        partner.addFriend(student);

                        if (student.isInFaction() && !student.getFaction().getMemberBlackList().contains(partner))
                        {
                            partner.setFaction(student.getFaction());
                            student.getFaction().addMember(partner);
                        }
                    }
                    else if (student.isInFaction() && student.getFaction().equals(partner.getFaction()) &&
                            (!student.getFriendBlackList().contains(partner) && !partner.getFriendBlackList().contains(student)))
                    {
                        student.addFriend(partner);
                        partner.addFriend(student);

                        if (followList.contains(student) || followList.contains(partner))
                            System.out.println(student.getFullName() + " became friends with " + partner.getFullName());

                        return;
                    }
                    else
                        return;

                    if (followList.contains(partner) || followList.contains(student))
                    {
                        if (student.isInFaction() && partner.isInFaction())
                            System.out.println(partner.getFullName() + " joined " + partner.getFaction().getName() + " and became friends with " + student.getFullName());
                        else
                            System.out.println(student.getFullName() + " became friends with " + partner.getFullName());
                    }

                    break;
                }
            }
        }
    }

    private static void fight(Student attacker, Student defender, boolean teamEnabled)
    {
        boolean printLog = false;

        if (followList.contains(attacker) || followList.contains(defender))
            printLog = true;

        boolean satisfied = false;
        List<Student> attackerAllies = new ArrayList<>();
        List<Student> defenderAllies = new ArrayList<>();
        int criticalNum = ((10 - attacker.getFood()) > attacker.getSelfishness() ? attacker.getSelfishness() : (10 - attacker.getFood()));
        int attackerTotal = 0;
        int defenderTotal = 0;
        int attackerDamage;
        int foodGained = 0;

        if (defender.getFood() > 0 && defender.getFood() < criticalNum)
            criticalNum = defender.getFood();
        else if (defender.getFood() == 0)
            criticalNum = 1;

        if (teamEnabled)
        {
            if (attacker.isCanSteal() && defender.getFood() > 0 && attacker.getAggression().equals("Agitated"))
            {
                switch ((int)(Math.random() * 3) + 1)
                {
                    case 1:
                        if (printLog)
                            System.out.println(attacker.getFullName() + " tried to steal food from " + defender.getFullName() + ", but was caught");

                        if (defender.getGoodness() >= (int)(Math.random() * 10) + 1 && !defender.getTraits().contains("Evil"))
                        {
                            if (printLog)
                                System.out.print(defender.getFullName() + " was kind enough to forgive " + attacker.getFirstName());

                            if (defender.getFood() > defender.getSelfishness())
                            {
                                transferFood(attacker, defender, 1);

                                if (printLog)
                                    System.out.println(" and gave " + (attacker.isMale() ? "him" : "her") + " some food");
                            }
                            else
                                System.out.println();

                            return;
                        }

                        defender.addToBlacklist(attacker, false);
                        Student tempStudent = attacker;
                        attacker = defender;
                        defender = tempStudent;

                        criticalNum = ((10 - attacker.getFood()) > attacker.getSelfishness() ? attacker.getSelfishness() : (10 - attacker.getFood()));

                        if (defender.getFood() > 0 && defender.getFood() < criticalNum)
                            criticalNum = defender.getFood();
                        else if (defender.getFood() == 0)
                            criticalNum = 1;
                        break;
                    case 2:
                        if ((criticalNum/2) > 0)
                        {
                            transferFood(attacker, defender, criticalNum/2);

                            if (printLog)
                                System.out.println(attacker.getFullName() + " was almost caught stealing from " + defender.getFullName() + ". " +
                                        (attacker.isMale() ? "He" : "She") + " had to settle for only taking " + (criticalNum / 2) + " food");
                        }
                        else if (printLog)
                            System.out.println(attacker.getFullName() + " tried stealing from " + defender.getFullName() +
                                    ", but was unsuccessful");
                        return;
                    case 3:
                        if (printLog)
                            System.out.println(attacker.getFullName() + " was able to steal " + criticalNum + " food from " + defender.getFullName());

                        transferFood(attacker, defender, criticalNum);
                        return;
                }
            }

            if (defender.getMobility() && defender.getAgility() > attacker.getAgility() && (int)((Math.random() * 12) + 1) < (defender.getAgility() - attacker.getAgility()))
            {
                if (printLog)
                    System.out.println(attacker.getFullName() + " tried to attack " + defender.getFullName() + ", but " + defender.getFirstName() + " ran away.");

                move(defender);
                return;
            }

            if ((attacker.getFriends().size() > 0 || defender.getFriends().size() > 0))
            {

                attackerAllies.add(attacker);
                defenderAllies.add(defender);

                for (Student allySearch : attacker.getLocation().getStudentsInRoom())
                {
                    if (!allySearch.equals(attacker) && (attacker.getFriends().contains(allySearch) || (attacker.isInFaction() && attacker.getFaction().getMembers().contains(allySearch))))
                        attackerAllies.add(allySearch);
                }

                for (Student allySearch : defender.getLocation().getStudentsInRoom())
                {
                    if (!allySearch.equals(defender) && !attackerAllies.contains(allySearch) && (defender.getFriends().contains(allySearch) || (defender.isInFaction() && defender.getFaction().getMembers().contains(allySearch))))
                        defenderAllies.add(allySearch);
                }

                if ((attacker.getTraits().contains("Popular") && !defender.getTraits().contains("Popular")) ||
                        (!attacker.getTraits().contains("Popular") && defender.getTraits().contains("Popular")))
                {
                    if (attacker.getTraits().contains("Popular"))
                    {
                        for (Student ally : attacker.getLocation().getStudentsInRoom())
                        {
                            if (!defenderAllies.contains(ally) && !attackerAllies.contains(ally) && !ally.isInFaction())
                                attackerAllies.add(ally);
                        }
                    }
                    else
                    {
                        for (Student ally : defender.getLocation().getStudentsInRoom())
                        {
                            if (!defenderAllies.contains(ally) && !attackerAllies.contains(ally) && !ally.isInFaction())
                                defenderAllies.add(ally);
                        }
                    }
                }
            }

            if (defender.getFood() >= criticalNum && defenderAllies.size() >= attackerAllies.size() && Math.abs(attacker.getStrength() - defender.getStrength()) <= 3)
            {
                if ((attacker.getStrength() - defender.getStrength()) >= 5)
                {
                    transferFood(attacker, defender, defender.getFood());
                    defender.addToBlacklist(attacker, false);

                    if (printLog)
                        System.out.println(attacker.getFullName() + " intimidated " + defender.getFullName() + " into giving up all " +
                                (defender.isMale() ? "his" : "her") + " food");

                    return;
                }
                else if (defender.getTraits().contains("Creative"))
                {
                    switch ((int)(Math.random() * 3) + 1)
                    {
                        case 1:
                            transferFood(attacker, defender, criticalNum);
                            defender.addToBlacklist(attacker, false);

                            if (printLog)
                                System.out.println(attacker.getFullName() + " wanted to fight " + defender.getFullName() + ", but " +
                                        defender.getFirstName() + " talked " + (attacker.isMale() ? "him" : "her") + " out of it. " +
                                        defender.getFirstName() + " still had to give up " + criticalNum + " food");
                            break;
                        case 2:
                            transferFood(attacker, defender, criticalNum/2);

                            if (printLog)
                            {
                                System.out.print(defender.getFullName() + " was able to talk " + attacker.getFullName() +
                                        " out of a fight ");

                                if (criticalNum/2 > 0)
                                    System.out.println("and gave up " + criticalNum/2 + " food instead");
                                else
                                    System.out.println();
                            }
                            break;
                        case 3:
                            if (printLog)
                                System.out.println(attacker.getFullName() + " wanted to attack " + defender.getFullName() + ", but " +
                                        defender.getFirstName() + " was able to calm the tension and avoid any conflict");
                            break;
                    }
                    return;
                }
                else if (defender.getTraits().contains("Kind") || defender.getTraits().contains("Altruistic"))
                {
                    transferFood(attacker, defender, criticalNum);

                    if (printLog)
                        System.out.println(attacker.getFullName() + " was able to intimidate " + defender.getFullName() + " into giving up " +
                                criticalNum + " food");

                    defender.addToBlacklist(attacker, false);
                    return;
                }
            }
        }
        else
            criticalNum = 10;

        defender.addToBlacklist(attacker, false);

        if (attacker.getFriendBlackList().contains(defender))
            attacker.gainBattleTokens(2);

        if (defender.getFriendBlackList().contains(attacker))
            attacker.gainBattleTokens(2);

        if (defenderAllies.size() > 1 || attackerAllies.size() > 1)
        {
            teamFight(attackerAllies, defenderAllies, printLog);
            return;
        }

        while (!satisfied)
        {
            attackerDamage = ((attacker.getStrength() + attacker.getAgility())/4) + (((int)(Math.random() * 11) + attacker.getBattleTokens())/3);

            if (attackerDamage < 0)
                attackerDamage = 0;

            int beforeFood = attacker.getFood();

            attackerTotal += attackerDamage;
            defender.loseHealth(attackerDamage);
            transferFood(attacker, defender, attackerDamage);
            foodGained += beforeFood - attacker.getFood();

            if (defender.getHealthNum() <= 0)
            {
                if (attacker.getAggression().equals("Aggressive"))
                {
                    if (printLog)
                        System.out.println(attacker.getFullName() + " killed " + defender.getFullName() + " and salvaged " + (defender.isMale() ? "his " : "her ") + "corpse for food");

                    attacker.setFood(10);

                    if (defender.getTraits().contains("Fat"))
                        attacker.getLocation().setFoodAvailable(attacker.getLocation().getFoodAvailable() + 10);
                }
                else
                {
                    if (printLog)
                    {
                        System.out.println(defender.getFullName() + " was killed by " + attacker.getFullName() + ". ");

                        if (foodGained > 0)
                            System.out.print(attacker.getFirstName() + " stole " + foodGained
                                    + " food from " + defender.getFirstName());
                    }

                    if (defender.getFood() > 0)
                        attacker.getLocation().setFoodAvailable(attacker.getLocation().getFoodAvailable() + defender.getFood());

                    if (defender.getTraits().contains("Fat"))
                        attacker.getLocation().setFoodAvailable(attacker.getLocation().getFoodAvailable() + 20);
                    else
                        attacker.getLocation().setFoodAvailable(attacker.getLocation().getFoodAvailable() + 10);
                }

                die(defender);
                attacker.updateBattleStates(defenderTotal, printLog);
                return;
            }
            else
            {
                int defenderDamage = ((defender.getStrength() + defender.getAgility())/4) + (((int)(Math.random() * 11) + defender.getBattleTokens())/3);

                if (defenderDamage < 0)
                    defenderDamage = 0;

                defenderTotal += defenderDamage;
                attacker.loseHealth(defenderDamage);

                if (attacker.getHealthNum() <= 0)
                {
                    if (printLog)
                        System.out.print(attacker.getFullName() + " attacked " + defender.getFullName() + ", but was killed. ");

                    transferFood(defender, attacker, defenderTotal);

                    if (defender.getFood() == 0 && defender.getAggression().equals("Aggressive"))
                    {
                        if (printLog)
                            System.out.println(attacker.getFirstName() + " was salvaged.");

                        defender.setFood(10);

                        if (attacker.getTraits().contains("Fat"))
                            defender.getLocation().setFoodAvailable(defender.getLocation().getFoodAvailable() + 10);
                    }
                    else
                    {
                        if (printLog)
                            System.out.println();

                        if (attacker.getTraits().contains("Fat"))
                            defender.getLocation().setFoodAvailable(defender.getLocation().getFoodAvailable() + 20);
                        else
                            defender.getLocation().setFoodAvailable(defender.getLocation().getFoodAvailable() + 10);
                    }

                    die(attacker);
                    defender.updateBattleStates(attackerTotal, printLog);
                    return;
                }

                if (criticalNum <= attacker.getFood())
                    satisfied = true;
            }
        }

        if (printLog)
        {
            System.out.println(attacker.getFullName() + " got in a fight with " + defender.getFullName() + ": " + attacker.getFirstName() + " stole "
                    + criticalNum + " food from " + defender.getFirstName());
        }

        attacker.updateBattleStates(defenderTotal, printLog);

        if (attacker.getHealthVis().equals("Crippled") || attacker.getHealthVis().equals("Injured"))
            attacker.addToBlacklist(defender, false);

        defender.updateBattleStates(attackerTotal, printLog);
    }

    private static void teamFight(List<Student> attackersFull, List<Student> defendersFull, boolean printLog)
    {
        boolean satisfied = false;
        int criticalNum = ((10 - attackersFull.get(0).getFood()) > attackersFull.get(0).getSelfishness() ? attackersFull.get(0).getSelfishness() :
                (10 - attackersFull.get(0).getFood()));
        List<Student> attackers = new ArrayList<>(attackersFull);
        List<Student> defenders = new ArrayList<>(defendersFull);
        int[] attackerDamageTaken = new int[attackersFull.size()];
        int[] defenderDamageTaken = new int[defendersFull.size()];
        int attackerDamage;

        if (defenders.get(0).getFood() > 0 && defenders.get(0).getFood() < criticalNum)
            criticalNum = defenders.get(0).getFood();
        else if (defenders.get(0).getFood() == 0)
            criticalNum = 1;

        if (printLog)
        {
            String printAttackers = "";
            String printDefenders = "";

            switch (attackersFull.size())
            {
                case 1:
                    printAttackers = attackersFull.get(0).getFullName();
                    break;
                case 2:
                    printAttackers = attackersFull.get(0).getFullName() + " and " + attackersFull.get(1).getFullName();
                    break;
                case 3:
                    printAttackers = attackersFull.get(0).getFullName() + ", " + attackersFull.get(1).getFullName() + ", and " + attackersFull.get(2).getFullName();
                    break;
            }

            switch (defendersFull.size())
            {
                case 1:
                    printDefenders = defendersFull.get(0).getFullName();
                    break;
                case 2:
                    printDefenders = defendersFull.get(0).getFullName() + " and " + defendersFull.get(1).getFullName();
                    break;
                case 3:
                    printDefenders = defendersFull.get(0).getFullName() + ", " + defendersFull.get(1).getFullName() + ", and " + defendersFull.get(2).getFullName();
                    break;
            }

            System.out.println(printAttackers + " got in a fight with " + printDefenders);
        }

        while (!satisfied)
        {
            for (int i = 0; i < attackers.size(); i++)
            {
                Student opponent;

                if (defenders.size() > 0)
                {
                    if (i + 1 > defenders.size())
                        opponent = defenders.get(0);
                    else
                        opponent = defenders.get(i);
                }
                else
                    break;

                attackerDamage = ((attackers.get(i).getStrength() + attackers.get(i).getAgility()) / 4) + (((int) (Math.random() * 11) + attackers.get(i).getBattleTokens()) / 3);

                if (attackerDamage < 0)
                    attackerDamage = 0;

                if (i + 1 > defenders.size())
                    defenderDamageTaken[0] += attackerDamage;
                else
                    defenderDamageTaken[i] += attackerDamage;

                opponent.loseHealth(attackerDamage);
                transferFood(attackers.get(0), opponent, attackerDamage);

                if (opponent.getHealthNum() <= 0)
                {
                    if (printLog)
                        System.out.println(attackers.get(i).getFullName() + " killed " + opponent.getFullName());

                    defenders.remove(opponent);
                    die(opponent);
                }
            }

            if (defenders.size() == 0)
            {
                teamSalvage(attackers, attackersFull, defendersFull, printLog);
                break;
            }

            for (int i = 0; i < defenders.size(); i++)
            {
                Student opponent;

                if (attackers.size() > 0)
                {
                    if (i + 1 > attackers.size())
                        opponent = attackers.get(0);
                    else
                        opponent = attackers.get(i);
                }
                else
                    break;

                int defenderDamage = ((defenders.get(i).getStrength() + defenders.get(i).getAgility()) / 4) + (((int) (Math.random() * 11) + defenders.get(i).getBattleTokens()) / 3);

                if (defenderDamage < 0)
                    defenderDamage = 0;

                if (i + 1 > attackers.size())
                    attackerDamageTaken[0] += defenderDamage;
                else
                    attackerDamageTaken[i] += defenderDamage;

                opponent.loseHealth(defenderDamage);

                if (!defenders.get(i).equals(defendersFull.get(0)) && defenders.get(i).getFood() < 10)
                    transferFood(defenders.get(i), opponent, defenderDamage);

                if (opponent.getHealthNum() <= 0)
                {
                    transferFood(defenders.get(i), opponent, (i + 1 > attackers.size() ? attackerDamageTaken[0] : attackerDamageTaken[i]));

                    if (opponent.getFood() > 0)
                        opponent.getLocation().setFoodAvailable(opponent.getLocation().getFoodAvailable() + opponent.getFood());

                    if (printLog)
                        System.out.println(opponent.getFullName() + " was killed by " + defenders.get(i).getFullName());

                    attackers.remove(opponent);
                    die(opponent);
                }
            }

            if (attackers.size() == 0)
            {
                teamSalvage(defenders, defendersFull, attackersFull, printLog);
                break;
            }

            if (attackers.size() > 0 && (criticalNum <= attackers.get(0).getFood() || !attackersFull.get(0).equals(attackers.get(0))))
            {
                teamSalvage(attackers, attackersFull, defendersFull, printLog);
                satisfied = true;
            }
        }

        for (int i = 0; i < attackersFull.size(); i++)
        {
            if (attackersFull.get(i).getHealthNum() > 0)
            {
                attackersFull.get(i).updateBattleStates(attackerDamageTaken[i], printLog);

                if ((attackersFull.get(i).getHealthVis().equals("Crippled") || attackersFull.get(i).getHealthVis().equals("Injured")) && defenders.size() > 0)
                    attackersFull.get(i).addToBlacklist(defenders.get(0), false);
            }
        }

        for (int i = 0; i < defendersFull.size(); i++)
        {
            if (defendersFull.get(i).getHealthNum() > 0)
            {
                defendersFull.get(i).updateBattleStates(defenderDamageTaken[i], printLog);

                if ((defendersFull.get(i).getHealthVis().equals("Crippled") || defendersFull.get(i).getHealthVis().equals("Injured")) && attackers.size() > 0)
                    defendersFull.get(i).addToBlacklist(attackers.get(0), false);
            }
        }
    }

    private static void teamSalvage(List<Student> winners, List<Student> winnersFull, List<Student> losersFull, boolean printLog)
    {
        int spoils = 0;

        for (Student attacker : losersFull)
        {
            if (attacker.getHealthNum() <= 0)
            {
                if (attacker.getTraits().contains("Fat"))
                    spoils += 20;
                else
                    spoils += 10;

                boolean eaten = false;

                for (Student defender : winners)
                {
                    if (defender.getAggression().equals("Aggressive") && defender.getFood() == 0)
                    {
                        if (!eaten && printLog)
                            System.out.println(attacker.getFullName() + " was salvaged");

                        eaten = true;

                        if (spoils >= 10)
                        {
                            spoils -= 10;
                            defender.setFood(10);
                        }
                        else
                        {
                            defender.setFood(defender.getFood() + spoils);
                            spoils = 0;
                        }
                    }
                }
            }
        }

        for (Student defender : winnersFull)
        {
            if (defender.getHealthNum() <= 0)
            {
                if (defender.getTraits().contains("Fat"))
                    spoils += 20;
                else
                    spoils += 10;
            }
        }

        winners.get(0).getLocation().setFoodAvailable(winners.get(0).getLocation().getFoodAvailable() + spoils);
    }

    private static void move(Student student)
    {
        if (student.getMobility())
            student.assignLocation(locations.changeRooms(student));
    }

    private static void transferFood(Student attacker, Student defender, int attackerDamage)
    {
        int foodGained;

        if (attackerDamage > defender.getFood())
        {
            foodGained = defender.getFood();
            defender.setFood(0);
        }
        else
        {
            foodGained = attackerDamage;
            defender.setFood(defender.getFood() - attackerDamage);
        }

        attacker.setFood(attacker.getFood() + foodGained);

        if (attacker.getFood() > 10)
        {
            attacker.getLocation().setFoodAvailable(attacker.getLocation().getFoodAvailable() + (attacker.getFood() - 10));
            attacker.setFood(10);
        }
    }

    private static void romance(Student initiator, Student partner)
    {
        if (!initiator.isRomanced() && !partner.isRomanced() && ((initiator.getTraits().contains("Ugly") && partner.getTraits().contains("Ugly")) || (!initiator.getTraits().contains("Ugly") && !partner.getTraits().contains("Ugly"))))
        {
            int differences = Math.abs(initiator.getBaseStrength() - partner.getBaseStrength()) + Math.abs(initiator.getAgility() - partner.getAgility())
                    + Math.abs(initiator.getCreativity() - partner.getCreativity()) + Math.abs(initiator.getGoodness() - partner.getGoodness())
                    + Math.abs(initiator.getSelfishness() - partner.getSelfishness());

            if (initiator.getTraits().contains("Kind"))
                differences -= 5;

            if (initiator.getTraits().contains("Altruistic"))
                differences -= 5;

            if (initiator.getTraits().contains("Handsome") || initiator.getTraits().contains("Pretty") || (int)(Math.random() * 50) + 1 > differences)
            {
                initiator.romance(partner);
                partner.romance(initiator);

                initiator.increaseStat();
                partner.increaseStat();

                if (followList.contains(partner) || followList.contains(initiator))
                    System.out.println(initiator.getFirstName() + " and " + partner.getFirstName() + " are now in a relationship");
            }
        }
    }
}

class Faction
{
    private String name;
    private List<Student> members = new ArrayList<>();
    private Student leader;
    private int factionFood = 0;
    private List<Student> memberBlackList = new ArrayList<>();
    private Room headquarters;
    private boolean securedRoom = false;

    Faction(Student leader)
    {
        String[] noun = {"Dogs", "Destroyers", "Eagles", "Tigers", "Birds", "Dominators", "Hawks", "Marines", "Devastators",
                "Lions", "Warriors", "Slayers", "Wizards", "Sharks", "Jets", "Drifters", "Dragons", "Cobras", "Hydras", "Hornets", "Wolves"};
        String[] noun2 = {"War", "Death", "Victory", "Kings", "Empires", "Wrath", "Rage", "Conquest", "Dominance", "Extinction", "Denial", "Frontiers",
                "Destruction", "Rampage", "Doom", "Darkness", "Light", "Liberty", "Survival", "Honor", "Valor", "Courage", "Resistance", "Thunder",
                "Lightning", "the Sun", "Dawn", "Daybreak", "the Night", "the Day", "Forces", "the Elements", "Fire", "Annihilation"};
        String[] adjective = {"Screaming", "Diamond", "Deadly", "Stormy", "Raging", "Starving", "Dark", "Black", "Fighting", "Free", "Invisible",
                "Doom", "Brave", "White", "Bronze", "Platinum", "Burning", "Vengeful", "Unrelenting", "Magma", "Sneaky", "Nuclear", "Radioactive"};

        if ((int)(Math.random() * 2) + 1 == 1)
            name = noun[(int)(Math.random() * noun.length)] + " of " + noun2[(int)(Math.random() * noun2.length)];
        else
            name = adjective[(int)(Math.random() * adjective.length)] + " " + noun[(int)(Math.random() * noun.length)];

        this.leader = leader;

        for (Student enemy : leader.getFriendBlackList())
        {
            if (enemy.getType().equals(leader.getType()))
                memberBlackList.add(enemy);
        }

        members.add(leader);
    }

    void secureRoom(Room headquarters)
    {
        this.headquarters = headquarters;
        securedRoom = true;
    }

    void removeHeadquarters()
    {
        securedRoom = false;
        headquarters.removeControllingFaction();
        headquarters = null;
    }

    Room getHeadquarters() { return headquarters; }
    boolean securedRoom() { return  securedRoom; }
    private void clearBlackList() { memberBlackList = new ArrayList<>(); }
    List<Student> getMemberBlackList() { return memberBlackList; }
    int getFactionPower() { return (members.size() * 10) + (factionFood * 2); }

    void setFactionLeader(Student leader)
    {
        clearBlackList();

        if (leader.getFriendBlackList().size() > 0)
        {
            for (Student enemy : leader.getFriendBlackList())
            {
                if (enemy.getType().equals(leader.getType()))
                {
                    memberBlackList.add(enemy);

                    if (enemy.isInFaction() && enemy.getFaction().equals(leader.getFaction()))
                        enemy.exile();
                }
            }
        }

        this.leader = leader;
    }

    Student getLeader() { return leader; }
    String getName() { return name; }
    List<Student> getMembers() { return members; }
    void addMember(Student member) { members.add(member); }
    void removeMember(Student member) { members.remove(member); }
    int getFactionFood() { return factionFood; }
    void removeFromBlacklist(Student student) { memberBlackList.remove(student); }
    void addFactionFood(int food) { factionFood += food; }

    void removeFactionFood(int food)
    {
        factionFood -= food;

        if (factionFood < 0)
            factionFood = 0;
    }

    void blacklistMember(Student student)
    {
        memberBlackList.add(student);

        if (student.isInFaction())
            student.exile();
    }
}

class Student
{
    private String firstName;
    private String lastName;
    private boolean isMale;
    private int food = 3;
    private int healthNum = 10;
    private String healthVis = "Viable";
    private int hungerNum = 0;
    private String hungerVis = "Well-Fed";
    private String aggression = "Passive";
    private int strength;
    private int agility;
    private int creativity;
    private int goodness;
    private int selfishness;
    private Room location;
    private String type;
    private List<String> traits = new ArrayList<>();
    private boolean canSteal;
    private boolean inFaction = false;
    private int battleTokens = 0;
    private boolean mobility = true;
    private boolean isRomanced = false;
    private int currency = 0;
    private Faction faction;
    private List<Student> friends = new ArrayList<>();
    private boolean isHiding = false;
    private Student romancePartner = null;
    private int baseStrength;
    private List<Student> friendBlackList = new ArrayList<>();

    Student()
    {
        String[] maleFirstNames = {"Andrew", "Bob", "Marvin", "Gavin", "Adam", "David", "Marcus", "Jordan", "Rey", "Lee", "Luke", "Michael", "Jay",
                "Jack", "Joe", "Peter", "Nick", "Harrison", "Matthew", "Wade", "Damien", "Jared", "Ralph", "Sam", "Ned", "Rob", "Brock", "Ryan",
                "Kyle", "Fred", "Steven", "Martin", "Norman", "Fernando", "Evan", "Ian", "Alexander", "Christian", "Chris", "Leonard", "Chance",
                "Josh", "Henry", "Will", "Jake", "Cliff", "John", "Alfred", "Quintin", "George", "Charles", "Max", "Robert", "Harry", "Liam",
                "Luke", "James", "Phillip", "Justin", "Jeremy", "Ken", "Ed", "Blaze", "Jonas", "Gino", "Stephan", "Bill", "William", "Nathan",
                "Paul", "Richard", "Joseph", "Donald", "Kevin", "Brian", "Ronald", "Jeffry", "Jacob", "Larry", "Scott", "Brandon", "Benjamin"};

        String[] femaleFirstNames = {"Jasmine", "Lily", "Joyce", "Allison", "June", "Alice", "Vanessa", "Jennifer", "Kara", "Tara", "Sophia", "Anna",
                "Grace", "Chloe", "Samantha", "Suzanne", "Jackie", "Leslie", "Stacy", "Joyce", "Vivian", "Mary", "Maria", "Nina", "Katie", "Fiona",
                "Alexandra", "Nicky", "Lauren", "Megan", "Claudia", "Jamie", "May", "Sammie", "Reese", "Sadie", "Elizabeth", "Cathy", "Lisa",
                "Elena", "Jessica", "Ashley", "Emily", "Rachel", "Angelica", "Eliza", "Eva", "Lucy", "Abbie", "Amanda", "Elanor", "Ann",
                "Kat", "Stephanie", "Jenna", "Dakota", "Mira", "Paulina", "Erica", "Daniella", "Kylie", "Rosaline", "Gertrude", "Ruth",
                "Cindy", "Emma", "Bella", "Ava", "Olivia", "Isabella", "Mia", "Charlotte", "Amelia", "Evelyn", "Harper", "Ella", "Avery", "Madison"};

        String[] lastNames = {"Dalton", "Smith", "Baker", "Grey", "Rose", "Snyder", "Strider", "Octavius", "Staplton", "Renae", "White", "Carleton",
                "Potter", "Carter", "Zed", "Barker", "Parker", "Long", "Little", "Freelander", "Turner", "Spot", "Strange", "Stover", "Vaughn",
                "Davidson", "Parks", "McNeil", "Fernandez", "Stevenson", "Tanner", "Walls", "Mills", "Wesley", "Washington", "Stringer", "Ford",
                "Fay", "Broder", "Ripley", "Walker", "Spark", "Bolt", "Morrow", "Fox", "Hamilton", "Adams", "McCarthy", "York", "Marks",
                "Darby", "West", "Sun", "Renner", "Osborne", "Sky", "Olson", "Burlingham", "Moore", "Roth", "Collins", "Freedman", "Pinson",
                "Dashner", "Johnson", "Williams", "Jones", "Brown", "Davis", "Miller", "Wilson", "Ross", "Taylor", "Anderson", "Thomas", "Jackson"};

        if ((int)(Math.random() * 2) == 1)
        {
            firstName = maleFirstNames[(int)(Math.random() * maleFirstNames.length)];
            isMale = true;
        }
        else
        {
            firstName = femaleFirstNames[(int)(Math.random() * femaleFirstNames.length)];
            isMale = false;
        }

        lastName = lastNames[(int)(Math.random() * lastNames.length)];

        strength = (int)(Math.random() * 10) + 1;
        baseStrength = strength;
        agility = (int)(Math.random() * 10) + 1;
        creativity = (int)(Math.random() * 10) + 1;
        goodness = (int)(Math.random() * 10) + 1;
        selfishness = (int)(Math.random() * 10) + 1;

        determineType();
        determineTraits();
    }

    Student(String firstName, String lastName, boolean isMale, int strength, int agility, int creativity, int goodness, int selfishness)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isMale = isMale;

        this.strength = strength;
        baseStrength = strength;
        this.agility = agility;
        this.creativity = creativity;
        this.goodness = goodness;
        this.selfishness = selfishness;

        determineType();
        determineTraits();
    }

    void printStudent()
    {
        System.out.println("Name: " + firstName + " " + lastName);
        System.out.println("Gender: " + (isMale ? "Male" : "Female"));
        System.out.println("Personality Type: " + type);

        String traitsVis = "";

        if (traits.size() == 1)
            traitsVis = traits.get(0);
        else if (traits.size() == 2)
            traitsVis = traits.get(0) + " and " + traits.get(1);
        else
        {
            for (int i = 0; i < traits.size(); i++)
            {
                if (i == traits.size() - 1)
                {
                    traitsVis += "and " + traits.get(i);
                    break;
                }

                traitsVis += traits.get(i) + ", ";
            }
        }

        System.out.println("Personality Traits: " + (traitsVis.equals("") ? "None" : traitsVis));
        System.out.println("Location: " + location.getName() + " " + (isHiding ? "(Hidden)" : "(Exposed)"));

        String friendsVis = "";

        if (friends.size() == 1)
            friendsVis = friends.get(0).getFullName();
        else if (friends.size() == 2)
            friendsVis = friends.get(0).getFullName() + " and " + friends.get(1).getFullName();
        else
        {
            for (int i = 0; i < friends.size(); i++)
            {
                if (i == friends.size() - 1)
                {
                    friendsVis += "and " + friends.get(i).getFullName();
                    break;
                }

                friendsVis += friends.get(i).getFullName() + ", ";
            }
        }

        System.out.println("Friends: " + (friendsVis.equals("") ? "None" : friendsVis));
        System.out.println("Relationship: " + (romancePartner == null ? "None" : romancePartner.getFullName()));

        String enemiesVis = "";

        if (friendBlackList.size() == 1)
            enemiesVis = friendBlackList.get(0).getFullName();
        else if (friendBlackList.size() == 2)
            enemiesVis = friendBlackList.get(0).getFullName() + " and " + friendBlackList.get(1).getFullName();
        else
        {
            for (int i = 0; i < friendBlackList.size(); i++)
            {
                if (i == friendBlackList.size() - 1)
                {
                    enemiesVis += "and " + friendBlackList.get(i).getFullName();
                    break;
                }

                enemiesVis += friendBlackList.get(i).getFullName() + ", ";
            }
        }

        System.out.println("Enemies: " + (enemiesVis.equals("") ? "None" : enemiesVis));
        System.out.println();
        System.out.println("Faction Name: " + (inFaction ? faction.getName() : "None"));

        String membersVis = "";

        if (inFaction)
        {
            if (faction.getMembers().size() == 1)
                membersVis = faction.getMembers().get(0).getFullName();
            else if (faction.getMembers().size() == 2)
                membersVis = faction.getMembers().get(0).getFullName() + " and " + faction.getMembers().get(1).getFullName();
            else
            {
                for (int i = 0; i < faction.getMembers().size(); i++)
                {
                    if (i == faction.getMembers().size() - 1)
                    {
                        membersVis += "and " + faction.getMembers().get(i).getFullName();
                        break;
                    }

                    membersVis += faction.getMembers().get(i).getFullName() + ", ";
                }
            }
        }
        else
            membersVis = "None";

        System.out.println("Faction Members: " + membersVis);
        System.out.println("Faction Leader: " + (inFaction ? faction.getLeader().getFullName() : "None"));
        System.out.println();
        System.out.println("Strength: " + strength + " / " + baseStrength);
        System.out.println("Agility: " + agility);
        System.out.println("Creativity: " + creativity);
        System.out.println("Goodness: " + goodness);
        System.out.println("Selfishness: " + selfishness);
        System.out.println();
        System.out.println("Health: " + healthNum + " / 10");
        System.out.println("Food: " + food + " / 10");
        System.out.println("Health State: " + healthVis);
        System.out.println("Days Without Food: " + hungerNum);
        System.out.println("Hunger State: " + hungerVis);
        System.out.println("Aggression: " + aggression);
        System.out.println("Battle Tokens: " + battleTokens);
        System.out.println("\n---------------------------");
    }

    private void determineType()
    {
        int highest = strength;
        String highestVis = "Strength";
        int lowest = strength;
        String lowestVis = "Strength";

        if (highest < agility)
        {
            highestVis = "Agility";
            highest = agility;
        }
        if (highest < creativity)
            highestVis = "Creativity";

        if (lowest > agility)
        {
            lowest = agility;
            lowestVis = "Agility";
        }
        if (lowest > creativity)
            lowestVis = "Creativity";

        if ((int)(Math.random() * 2) == 1)
        {
            switch (highestVis)
            {
                case "Strength":
                    type = "Jock";
                    break;
                case "Agility":
                    type = "Independent";
                    break;
                case "Creativity":
                    type = "Artist";
            }
        }
        else
        {
            switch (lowestVis)
            {
                case "Strength":
                    type = "Gamer";
                    break;
                case "Agility":
                    type = "Nerd";
                    break;
                case "Creativity":
                    type = "Outcast";
            }
        }
    }

    void setFaction(Faction faction)
    {
        this.faction = faction;
        inFaction = true;

        if (getFullName().equals(faction.getLeader().getFullName()))
        {
            for (int i = 0; i < friends.size(); i++)
            {
                if ((friends.get(i).isInFaction() && friends.get(i).getFaction().getLeader().equals(friends.get(i))))
                    continue;
                else if (friends.get(i).isInFaction() && !friends.get(i).getFriends().contains(friends.get(i).getFaction().getLeader()))
                    friends.get(i).getFaction().removeMember(friends.get(i));
                else if (friends.get(i).isInFaction())
                {
                    if (!isRomanced && !friends.get(i).equals(romancePartner))
                    {
                        friends.get(i).removeFriend(Student.this);
                        removeFriend(friends.get(i));
                        i--;
                        continue;
                    }
                    else
                        friends.get(i).getFaction().removeMember(friends.get(i));
                }

                friends.get(i).setFaction(faction);

                if (!faction.getMembers().contains(friends.get(i)))
                    faction.addMember(friends.get(i));
            }
        }
    }

    private void determineTraits()
    {
        if (selfishness >= 8 && !type.equals("Outcast"))
            traits.add("Popular");

        if (strength > agility + 3 && !traits.contains("Popular"))
            traits.add("Shy");

        if (selfishness <= 3)
            traits.add("Altruistic");

        if (goodness <= 2)
        {
            traits.add("Evil");
            canSteal = true;
        }

        if (creativity + agility >= 14)
        {
            traits.add("Smart");
            canSteal = true;
        }

        if (agility <= 3)
            traits.add("Fat");

        if (creativity >= 7)
            traits.add("Creative");

        if (goodness >= 8)
            traits.add("Kind");

        if (agility + selfishness <= 8)
            traits.add("Ugly");

        if (agility + selfishness >= 16 && !type.equals("Outcast"))
            traits.add((isMale ? "Handsome" : "Pretty"));

    }

    String getAggression() {
        return aggression;
    }
    void assignLocation(Room room) { location = room; }
    String getFullName() { return firstName + " " + lastName; }
    boolean isInFaction() { return inFaction; }

    void removeFriend (Student friend)
    {
        friends.remove(friend);

        if (isRomanced && romancePartner.equals(friend))
            unRomance();
    }

    int getCreativity() { return creativity; }
    int getGoodness() { return goodness; }
    String getHungerVis() { return hungerVis; }
    void gainHealth(int gain) { this.healthNum += gain; }
    int getBaseStrength() { return baseStrength; }
    List<Student> getFriendBlackList() { return friendBlackList; }
    void removeFromBlacklist(Student student) { friendBlackList.remove(student); }

    void guaranteeAddToBlacklist(Student student)
    {
        if (!friendBlackList.contains(student))
        {
            friendBlackList.add(student);
            friends.remove(student);
        }

    }

    void exile()
    {
        faction.removeMember(Student.this);
        inFaction = false;
        faction = null;
    }

    void addToBlacklist(Student student, boolean printLog)
    {
        if (!traits.contains("Kind") && !friendBlackList.contains(student) && (traits.contains("Evil") || (int)(Math.random() * 11) + 1 >= goodness))
        {
            friendBlackList.add(student);
            friends.remove(student);

            if (printLog)
                System.out.println(getFullName() + " became enemies with " + student.getFullName());
        }
    }

    private void setRomanceFalse()
    {
        isRomanced = false;
        friends.remove(romancePartner);
        romancePartner = null;
    }

    void unRomance ()
    {
        if (romancePartner != null)
        {
            romancePartner.setRomanceFalse();
            romancePartner = null;
            isRomanced = false;
        }

    }

    void romance(Student partner)
    {
        isRomanced = true;
        romancePartner = partner;
    }

    void eat()
    {
        hungerNum++;

        if (food > 0 && ((strength >= 7) || (strength >= 4 && hungerNum % 2 == 0) || (strength <= 3 && hungerNum % 3 == 0) || hungerNum > 3))
        {
            hungerNum = 0;
            food--;

            if (strength < baseStrength)
                strength++;
        }
        else
            gainHunger();
    }

    int getHealthNum() { return healthNum; }
    int getFood() { return food; }
    boolean isMale() { return isMale; }
    boolean isCanSteal() { return canSteal; }
    Faction getFaction() { return faction; }
    void loseHealth(int reduction) { healthNum -= reduction; }
    int getHungerNum() { return hungerNum; }
    void setMobility(boolean mobility) { this.mobility = mobility; }
    void gainBattleTokens(int gain) { battleTokens += gain; }

    void increaseStat()
    {
        if (baseStrength + agility + creativity < 30)
        {
            while (true)
            {
                int randomStat = (int)(Math.random() * 3) + 1;

                if (randomStat == 1 && baseStrength < 10)
                {
                    baseStrength++;
                    strength++;
                    break;
                }
                else if (randomStat == 2 && agility < 10)
                {
                    agility++;
                    break;
                }
                else if (randomStat == 3 && creativity < 10)
                {
                    creativity++;
                    break;
                }
            }
        }
        if (baseStrength < 10)
        {
            strength++;
            baseStrength++;
        }
        else if (agility < 10)
            agility++;
        else if (creativity < 10)
            creativity++;
    }

    void updateStates(boolean printLog)
    {
        battleTokens = 0;

        if (hungerNum == 0)
        {
            if (hungerVis.equals("Starving") && printLog)
                System.out.println(firstName + " " + lastName + " is no longer starving");

            hungerVis = "Well-Fed";
            battleTokens++;
        }
        else if (hungerNum >= 15 - baseStrength)
        {
            if (!hungerVis.equals("Starving") && printLog)
                System.out.println(firstName + " " + lastName + " is now starving");

            hungerVis = "Starving";
        }
        else
            hungerVis = "Nourished";

        if ((hungerVis.equals("Starving") || (food < (selfishness / 2) && hungerNum >= 3)) && !traits.contains("Kind"))
        {
            if ((traits.contains("Evil") && hungerNum >= 10) || hungerNum >= 15 || healthNum <= 3)
            {
                aggression = "Aggressive";
                battleTokens += 3;
            }
            else
                aggression = "Agitated";
        }
        else
        {
            aggression = "Passive";
            battleTokens--;
        }

        if (healthVis.equals("Injured") && healthNum > 6)
        {
            healthVis = "Viable";

            if (printLog)
                System.out.println(firstName + " " + lastName + " has made a recovery");
        }

        if (healthVis.equals("Patched"))
            battleTokens -= 5;

        if (healthVis.equals("Crippled"))
            battleTokens -= 10;

        if (healthVis.equals("Injured"))
            battleTokens -= 3;

        if (traits.contains("Evil"))
            battleTokens += 3;
    }

    void updateBattleStates(int damageTaken, boolean printLog)
    {
        if (damageTaken >= 7)
        {
            if (printLog)
                System.out.println(firstName + " is now crippled");

            healthVis = "Crippled";
            mobility = false;
            return;
        }

        if (healthNum <= 6 && !healthVis.equals("Patched"))
        {
            if (healthVis.equals("Viable") && printLog)
                System.out.println(firstName + " is now injured");

            healthVis = "Injured";
        }

        updateStates(false);
    }

    int getSelfishness() { return selfishness; }
    List<String> getTraits() { return traits; }
    Room getLocation() { return location; }
    String getType() { return type; }
    void addFriend(Student student) { friends.add(student); }

    List<Student> getFriends() { return friends; }
    boolean isRomanced() { return isRomanced; }

    void takeFood()
    {
        if (location.getFoodAvailable() >= (10 - food))
        {
            location.setFoodAvailable(location.getFoodAvailable() - (10 - food));
            food = 10;
        }
        else
        {
            food += location.getFoodAvailable();
            location.setFoodAvailable(0);
        }
    }

    private void gainHunger()
    {
        if (hungerVis.equals("Starving"))
            healthNum--;

        if (strength >= 7 || (strength >= 4 && hungerNum % 2 == 0) || (strength <= 3 && hungerNum % 3 == 0))
        {
            if (food > 0)
            {
                hungerNum = 2;
                eat();
                return;
            }

            strength--;

            if (strength < 1)
                strength = 1;
        }
    }

    int getPower() { return strength + agility + creativity; }
    boolean hasTrait(String trait) { return traits.indexOf(trait) != -1; }
    int getAgility() { return agility; }
    boolean getMobility() { return mobility; }
    int getStrength() { return strength; }
    int getBattleTokens() { return battleTokens; }
    void setFood(int food) { this.food = food; }
    String getFirstName() { return firstName; }
    String getHealthVis() { return healthVis; }
    boolean isHiding() { return isHiding; }
    void setHiding(boolean hiding) { isHiding = hiding; }
    void setHealthVis(String healthVis) { this.healthVis = healthVis; }
    Student getRomancePartner() { return romancePartner; }
}

class Locations
{
    private List<Room> locations = new ArrayList<>();

    Locations()
    {
        locations.add(new Room("Courtyard"));
        locations.add(new Room("Auditorium"));
        locations.add(new Room("Gym"));
        locations.add(new Room("Cafeteria"));
        locations.add(new Room("History Classroom"));
        locations.add(new Room("Biology Classroom"));
        locations.add(new Room("Chemistry Classroom"));
        locations.add(new Room("Administrative Offices"));
        locations.add(new Room("Main Office"));
        locations.add(new Room("Computer Lab"));
        locations.add(new Room("Boys Bathroom"));
        locations.add(new Room("Girls Bathroom"));
        locations.add(new Room("Math Classroom"));
        locations.add(new Room("Physics Classroom"));
        locations.add(new Room("English Classroom"));
        locations.add(new Room("Library"));
    }

    Room addToRandomRoom(Student student)
    {
        while(true)
        {
            int roomNum = (int)(Math.random() * 16);

            if (locations.get(roomNum).getOccupancy() <= 3)
            {
                locations.get(roomNum).addPeople(student);
                return locations.get(roomNum);
            }
        }
    }

    void viewRooms()
    {
        for (Room room : locations)
        {
            System.out.println(room.getName() + ": " + (room.getOccupancy() + room.getNumOfHiders()) + " : " + room.getFoodAvailable());
            System.out.println("Controlled By: " + (room.isSecured() ? room.getControllingFaction().getName() : "No one"));
            room.listStudents();
            System.out.println("\nHiders:");
            room.listHiders();

            System.out.println("--------------------------");
        }
    }

    Room changeRooms(Student student)
    {
        while(true)
        {
            int roomNum = (int)(Math.random() * 16);

            if (locations.get(roomNum).getOccupancy() <= 3 && !student.getLocation().equals(locations.get(roomNum)))
            {
                student.getLocation().removePeople(student);
                locations.get(roomNum).addPeople(student);

                return locations.get(roomNum);
            }
        }
    }

    Room goToRoomWithFood(Student student)
    {
        Room newRoom = student.getLocation();

        for (Room room : locations)
        {
            if (room.getOccupancy() < 4 && room.getFoodAvailable() > newRoom.getFoodAvailable())
                newRoom = room;
        }

        if (!student.getLocation().equals(newRoom))
        {
            student.getLocation().removePeople(student);
            newRoom.addPeople(student);
        }

        return newRoom;
    }

    Room goToSpecificRoom(Student student, int roomIndex)
    {
        if (locations.get(roomIndex).getOccupancy() <= 3)
        {
            student.getLocation().removePeople(student);
            locations.get(roomIndex).addPeople(student);

            return locations.get(roomIndex);
        }

        else
            return changeRooms(student);
    }

    void supplyDrop(int numOfStudents) { locations.get(0).setFoodAvailable(numOfStudents + locations.get(0).getFoodAvailable()); }
    void removeFromMap(Student student) { student.getLocation().removePeople(student); }
    int foodInCourtyard() { return locations.get(0).getFoodAvailable(); }

    Room goToEmptyRoom(Student student)
    {
        if (student.getLocation().getOccupancy() > 1)
        {
            for (int i = 0; i < 16; i++)
            {
                if (locations.get(i).getOccupancy() == 0)
                {
                    student.getLocation().removePeople(student);
                    locations.get(i).addPeople(student);
                    return locations.get(i);
                }
            }

            return student.getLocation();
        }

        else
            return student.getLocation();
    }
}

class Room
{
    private boolean isSecured = false;
    private Faction controllingFaction;
    private int Occupancy = 0;
    private String name;
    private int foodAvailable = 0;
    private List<Student> studentsInRoom = new ArrayList<>();
    private List<Student> hidingList = new ArrayList<>();

    Room(String name) { this.name = name; }
    boolean isSecured() { return isSecured; }
    void setFoodAvailable(int foodAvailable) { this.foodAvailable = foodAvailable; }

    void setControllingFaction(Faction controllingFaction)
    {
        this.controllingFaction = controllingFaction;
        isSecured = true;
    }

    void removeControllingFaction()
    {
        isSecured = false;
        controllingFaction = null;
    }

    Faction getControllingFaction() { return controllingFaction; }

    void addPeople(Student student)
    {
        Occupancy++;
        studentsInRoom.add(student);
    }

    void removePeople(Student student)
    {
        Occupancy--;
        studentsInRoom.remove(student);
    }

    String getName() { return name; }
    int getOccupancy() { return Occupancy; }

    void listStudents()
    {
        for (Student student : studentsInRoom)
        {
            System.out.println(student.getFullName());
        }
    }

    int getFoodAvailable() { return foodAvailable; }
    List<Student> getHiders() { return hidingList; }

    Student getStudentWithMostFood(Student yourself)
    {
        Student mostFood = studentsInRoom.get(0);

        for (Student student : studentsInRoom)
        {
            if (mostFood.getFood() < student.getFood() && !student.getFullName().equals(yourself.getFullName()))
                mostFood = student;
        }

        if (mostFood.getFood() == 0)
        {
            for (Student student : studentsInRoom)
            {
                if (student.getTraits().contains("Fat"))
                {
                    mostFood = student;
                    break;
                }
            }
        }

        return mostFood;
    }

    void addHider(Student student)
    {
        hidingList.add(student);
        student.setHiding(true);
        removePeople(student);
    }

    void removeHider(Student student)
    {
        hidingList.remove(student);
        student.setHiding(false);
        addPeople(student);
    }

    int getNumOfHiders() { return hidingList.size();}

    void listHiders()
    {
        for (Student hider : hidingList)
        {
            System.out.println(hider.getFullName());
        }
    }


    List<Student> getStudentsInRoom() { return studentsInRoom; }
}
