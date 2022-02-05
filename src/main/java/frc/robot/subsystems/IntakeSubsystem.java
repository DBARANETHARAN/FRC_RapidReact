// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import frc.robot.Constants.IDs;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
public class IntakeSubsystem extends SubsystemBase {
  

  private CANSparkMax m_intakeArmMotor = new CANSparkMax(Constants.IDs.CAN.INTAKE_MOTOR_ARM, MotorType.kBrushless);
  private SparkMaxPIDController m_pidController;
  private RelativeEncoder m_encoder;
  private static boolean m_enabled = false;

  public static final double DOWN_POSITION_DEG = 0.0;
  public static final double UP_POSITION_DEG = 90.0;
  boolean m_isUp = true;
  private double m_currentPosition_deg = UP_POSITION_DEG;
  public static final double COUNTS_PER_DEG = 42.0*200.0/360.0;// 42 counts per motor rev, 200:1 gear ration, 360 deg/rev
 
  private static final double BEATER_ON_SPEED = 0.2;
  private static final double BEATER_OFF_SPEED = 0.0;
  private boolean m_beaterOn = false;
  private CANSparkMax m_beaterMotor = new CANSparkMax (IDs.CAN.INTAKE_BEATER, MotorType.kBrushless);

  /** Creates a new IntakeSubsystem. */
  public IntakeSubsystem() {
    if (m_intakeArmMotor != null){
      m_enabled = true;
    }

    m_pidController = m_intakeArmMotor.getPIDController();
    m_encoder = m_intakeArmMotor.getEncoder();

    m_encoder.setPosition(0);
    m_intakeArmMotor.setIdleMode(IdleMode.kBrake);

    m_pidController.setP(0.05);
    m_pidController.setI(0);
    m_pidController.setD(0);
    m_pidController.setIZone(0);
    m_pidController.setFF(0);
    m_pidController.setOutputRange(-0.2, 0.2);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    m_pidController.setReference(toEncoder(m_currentPosition_deg), ControlType.kPosition);//todo: resume here
 
    if (m_beaterOn){
      m_beaterMotor.set(BEATER_ON_SPEED);
    }
    else {
      m_beaterMotor.set(BEATER_OFF_SPEED);
    }
 
  }

  private int toEncoder(double position_deg) {
    double value = (position_deg - UP_POSITION_DEG)*COUNTS_PER_DEG;
    return (int)value;
  }

  public boolean isUp() {
    return m_isUp;
  }


  public void setIsUp(boolean isUp){
    m_isUp = isUp;
  }

  public double getPosition_deg() {
    return m_currentPosition_deg;
  }

public void setPosition_deg(double position) {
  m_currentPosition_deg = position;
  SmartDashboard.putNumber("currentPosition: ", position);
}

    
  

  public void setBeaterOn(boolean on) {
    m_beaterOn = on;
  }     
}
