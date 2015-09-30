//Arduino Pinout
//Joystick X -> A3
//Joystick Y -> A5
//Joystick Button -> A6

//Servo Controls
//Bottom Servo -> D9
//Middle Servo -> D6
//Claw Servo -> D3
#include <Servo.h>

#define MS_DELAY 10

int MAX_CLAW_ANGLE = 110;
int MIN_CLAW_ANGLE = 40;

int MAX_X_VAL = 115;
int MIN_X_VAL = 65;

int XPIN = 3;
int YPIN = 4;
int BTN = 5;
int xval = 90;
int yval = 90;
boolean butVal = false;

Servo servo1;
Servo servo2;
Servo clawServo;

boolean pressed = false;
boolean prevPressed = false;
int increment = -1;
int pos = 60;
void setup() {
  // put your setup code here, to run once:
  servo1.attach(9);
  servo1.write(xval);
  servo2.attach(6);
  clawServo.attach(3);
  servo2.write(90);
  clawServo.write(60);
  pinMode(12, OUTPUT);
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  digitalWrite(12, HIGH);
  int val = analogRead(XPIN);
  int val1 = analogRead(YPIN);
  pressed = isButtonPressed();
  
  if (pressed == false && prevPressed == true) {
    Serial.println("Switching dir");
    if (increment == -1) {
      increment = 1;
    } else {
      increment = -1;
    }
  }

  moveClaw();
  
  if (val > 600 && xval < 180) {
    xval++;
  } else if (val < 400 && xval > 1) {
    xval--;
  }
    if (val1 > 600 && yval < 180) {
    yval++;
  } else if (val1 < 400 && yval > 1) {
    yval--;
  }

  if (xval >= MAX_X_VAL) {
    xval = MAX_X_VAL;
  }

  if (xval <= MIN_X_VAL) {
    xval = MIN_X_VAL;
  }
  
  servo1.write(xval);
  servo2.write(yval);
  prevPressed = pressed;
  delay(MS_DELAY);
}

boolean isButtonPressed() {
  int butVal = analogRead(BTN);
  boolean isPressed = false;
  
  if (butVal < 200) {
    isPressed = true;
  }

  return isPressed;
}

void moveClaw() {
  if (pos <= MIN_CLAW_ANGLE) {
    pos = MIN_CLAW_ANGLE;
  }

  if (pos >= MAX_CLAW_ANGLE) {
    pos = MAX_CLAW_ANGLE;
  }
  
  if (pressed && pos <= MAX_CLAW_ANGLE && pos >= MIN_CLAW_ANGLE) {
    Serial.println("Moving claw");
    pos = pos + increment;
    clawServo.write(pos);
  }
}
