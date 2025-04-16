import java.util.Scanner;

interface Command{
    void execute();
    void undo();
}
class Light {
    public void turnOn() {
        System.out.println("[Light] Turning ON");
    }

    public void turnOff() {
        System.out.println("[Light] Turning OFF");
    }
}

class Thermostat{
    private int temp = 10, previousTemp = 10;

    public void setTemperature(int temperature) {
        previousTemp = temp;
        temp = temperature;
        System.out.println("[Thermostat] Setting temperature to " + temperature + "°C");
    }

    public void revertTemperature() {
        System.out.println("[Thermostat] Reverting to previous temperature: " + previousTemp + "°C");
        temp = previousTemp;
    }
}

class SetThermostatCommand implements Command {
    protected int temp;
    private Thermostat thermostat;

    public SetThermostatCommand(Thermostat thermostat, int temp) {
        this.thermostat = thermostat;
        this.temp = temp;
    }

    @Override
    public void execute() {
        thermostat.setTemperature(temp);
    }

    @Override
    public void undo() {
        thermostat.revertTemperature();
    }
}

class TurnOnLightCommand implements Command {
    private Light light;

    public TurnOnLightCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.turnOn();
    }

    public void undo() {
        light.turnOff();
    }
}

class TurnOffLightCommand implements Command {
    private Light light;

    public TurnOffLightCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.turnOff();
    }

    public void undo() {
        light.turnOn();
    }
}

class SmartHomeRemoteControl {
    private Command[] slots = new Command[5];
    private Command lastCommand = null;

    public void setCommand(int slot, Command command) {
        if (slot >= 0 && slot < slots.length) {
            slots[slot] = command;
            System.out.println("Command set in slot " + slot);
        } else {
            System.out.println("Invalid slot number");
        }
    }

    public void pressButton(int slot) {
        if (slot >= 0 && slot < slots.length && slots[slot] != null) {
            slots[slot].execute();
            lastCommand = slots[slot];
        } else {
            System.out.println("[Remote] No command to slot " + slot);
        }
    }

    public void undoButton() {
        if (lastCommand != null) {
            System.out.println("Undo last command   ");
            lastCommand.undo();
            lastCommand = null;
        } else {
            System.out.println("No command to undo.");
        }
    }
}

public class CommandPattern {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        SmartHomeRemoteControl remote = new SmartHomeRemoteControl();
        Light light = new Light();
        Thermostat thermostat = new Thermostat();

        boolean running = true;

        while (running) {
            System.out.println("\nSmart Home Remote Control:");
            System.out.println("1. Assign TurnOnLight to Slot");
            System.out.println("2. Assign SetThermostat to Slot");
            System.out.println("3. Press Button");
            System.out.println("4. Undo Last Command");
            System.out.println("5. View Slot Assignments");
            System.out.println("0. Exit");
            System.out.print("Choose option: ");

            int option = in.nextInt();
            switch (option) {
                case 1:
                    System.out.print("Enter slot (0-4): ");
                    int slot1 = in.nextInt();
                    remote.setCommand(slot1, new TurnOnLightCommand(light));
                    break;
                case 2:
                    System.out.print("Enter slot (0-4): ");
                    int slot2 = in.nextInt();
                    System.out.print("Enter temperature: ");
                    int temp = in.nextInt();
                    remote.setCommand(slot2, new SetThermostatCommand(thermostat, temp));
                    break;
                case 3:
                    System.out.print("Enter slot (0-4): ");
                    int pressSlot = in.nextInt();
                    remote.pressButton(pressSlot);
                    break;
                case 4:
                    remote.undoButton();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }

    }
}
