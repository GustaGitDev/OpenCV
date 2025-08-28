package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

public class Main {
  public static void main(String[] args) {
    RobotBase.startRobot(Robot::new); // Passa a referência da classe Robot
  }
}