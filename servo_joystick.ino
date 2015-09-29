#include <Servo.h>

#define MS_DELAY 10

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
int pos = 180;
void setup() {
  // put your setup code here, to run once:
  servo1.attach(9);
  servo1.write(xval);
  servo2.attach(6);
  clawServo.attach(3);
  servo2.write(180);
  clawServo.write(50);
  pinMode(13, OUTPUT);
  Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
  digitalWrite(13, HIGH);
  int val = analogRead(XPIN);
  int val1 = analogRead(YPIN);
  pressed = isButtonPressed();
  
  if (pressed == false && prevPressed == true) {
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
  
  servo1.write(xval);
  servo2.write(yval);
  prevPressed = pressed;
  delay(MS_DELAY);
}

boolean isButtonPressed() {
  int butVal = analogRead(BTN);
  boolean isPressed = false;
  
  if (butVal < 90) {
    isPressed = true;
  }
  
  return isPressed;
}

void moveClaw() {
  if (pressed) {
    pos = pos + increment;
    clawServo.write(pos);
  }
}