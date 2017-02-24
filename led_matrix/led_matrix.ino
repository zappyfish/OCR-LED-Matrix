int latchPin = 8;
int latchPin2 = 4;
int latchPin3 = 7;
int latchPin4 = 2;
int latchPin5 = 10;
int latchPin6 = 13;

int latchAr[] = {latchPin, latchPin2, latchPin3, latchPin4,
latchPin5, latchPin6};

int clockPin = 12;

int dataPin1 = 11;
int dataPin2 = 3;
int dataPin3 = 5;
int dataPin4 = 6;
int dataPin5 = 9;
int dataPin6 = A5;

byte A[] = {
  B00011000,
  B00100100,
  B00111100,
  B00100100,
  B00100100,
  B00100100
};
byte B[] = {
  B01000000,
  B01000000,
  B01110000,
  B01010000,
  B01110000,
  B00000000
};
byte C[] = {
  B01111110,
  B01000000,
  B01000000,
  B01000000,
  B01000000,
  B01111110
};

byte D[] = {
  B00000010,
  B00000010,
  B00001110,
  B00001010,
  B00001110,
  B00000000
};
byte E[] = {
  B01111000,
  B01000000,
  B01111000,
  B01000000,
  B01111000,
  B00000000
};

byte F[] = {
  B01111000,
  B01000000,
  B01110000,
  B01000000,
  B01000000,
  B00000000
};


byte H[] = {
  B01001000,
  B01001000,
  B01111000,
  B01001000,
  B01001000,
  B00000000
};

byte K[] = {
  B01000010,
  B01000100,
  B01001000,
  B01110000,
  B01011000,
  B01000100  
};

byte L[] = {
  B01000000,
  B01000000,
  B01000000,
  B01000000,
  B01111000,
  B00000000
};

byte O[] = {
  B00000000,
  B00111100,
  B00100100,
  B00100100,
  B00111100,
  B00000000
};

byte W[] = {
  B00000000,
  B01000100,
  B01010100,
  B00101000,
  B00000000,
  B00000000
};

byte R[] = {
  B01110000,
  B01001000,
  B01110000,
  B01001000,
  B00000000,
  B00000000
};

byte empty[] = {
  B00000000,
  B00000000,
  B00000000,
  B00000000,
  B00000000,
  B00000000
};

int datAr[] = {dataPin1, dataPin2, dataPin3, dataPin4, dataPin5, dataPin6};

int x=0, y=0;

void setup() {
  // put your setup code here, to run once:
pinMode(latchPin, OUTPUT);
pinMode(latchPin2, OUTPUT);
pinMode(latchPin3, OUTPUT);
pinMode(latchPin4, OUTPUT);
pinMode(latchPin5, OUTPUT);
pinMode(latchPin6, OUTPUT);

pinMode(clockPin, OUTPUT);

pinMode(dataPin1, OUTPUT);
pinMode(dataPin2, OUTPUT);
pinMode(dataPin3, OUTPUT);
pinMode(dataPin4, OUTPUT);
pinMode(dataPin5, OUTPUT);
pinMode(dataPin6, OUTPUT);

Serial.begin(9600);
}

void loop() {
  // put your main code here, to run repeatedly:
//snake(150);
/*
draw(H);
delay(500);
draw(E);
delay(500);
draw(L);
delay(500);
draw(L);
delay(500);
draw(O);
delay(500);

draw(empty);
delay(1000);

draw(W);
delay(500);
draw(O);
delay(500);
draw(R);
delay(500);
draw(L);
delay(500);
draw(D);
delay(500);

draw(empty);
delay(1000);

*/

if(Serial.available()) {
  char c = Serial.read();
  /*switch(c) {
    case 'u':
      y -=1;
      if(y<0) {
        y = 5;
      }
      break;
    case 'd':
      y = (y+1)%6;
      break;
    case 'l':
      x -=1;
      if(x<0) {
        x = 5;
      }
      break;
    case 'r':
      x = (x+1)%6;
      break;
    case 'a':
      x = (x+1)%6;
      y = (y+1)%6;
  }
  drawDot(x, y);
  */
  if(c == 'a') {
    draw(A);
  }
  else if(c == '*') {
    draw(heart);
  }
}

}

void draw(byte b[]) {
  for(int i = 0; i<6; i++) {
    digitalWrite(latchAr[i], LOW);
    shiftOut(datAr[i], clockPin, LSBFIRST, b[i]);
    digitalWrite(latchAr[i], HIGH); 
  }
}

void drawDot(int x, int y) {
  draw(empty);
  digitalWrite(latchAr[y], LOW);
  shiftOut(datAr[y], clockPin, MSBFIRST, 2<<x);
  digitalWrite(latchAr[y], HIGH);
}

void snake(int tim) {
  int x[5];
  int y[5];
  int x_next;
  int y_next;
  boolean xory;
  boolean retry = false;
  int rando;

  byte b[] = {
    B00000000,
    B00000000,
    B00000000,
    B00000000,
    B00000000,
    B00000000
  };

  int start = millis();
  // start at the first row
  for(int i = 0; i<5; i++) {
    x[i] = i;
  }
  for(int i = 0; i<5; i++) {
    y[i] = 0;
  }

  for(int i = 0; i<5; i++) {
    b[y[i]] |= (1<<(6-x[i])); // put the values into the byte array
  }
  draw(b);
  while(millis()-start<tim*1000) {
    for(int i = 0; i<6; i++) {
      b[i] = B00000000;
    }
    xory = random(2);
    Serial.print("xory: ");Serial.println(xory);
    if(xory) {
      do{
      rando = (random(2) == 1? -1:1);
      x_next = rando+x[4];
      if(x_next<0) {x_next = 5;}
      else if(x_next>5){ x_next = 0;}
      retry = false;
        for(int i = 0; i< 4; i++) {
          if(x_next == x[i] && y[4] == y[i]) {
            retry = true;
            break;
          }
        
      }
      Serial.println("stuck in x");
      }while(retry);
      for(int i = 0; i<4; i++) {
        x[i] = x[i+1];
      }
      x[4] = x_next;
    }
    else {
      do{
      y_next = (random(2) == 1? -1:1)+y[4];
      if(y_next<0) y_next = 5;
      else if(y_next>5) y_next = 0;
      retry = false;
        for(int i = 0; i< 4; i++) {
          if(y_next == y[i] && x[4] == x[i]) {
            retry = true;
          }
        
      }
      Serial.println("stuck in y");
      }while(retry);
      for(int i = 0; i<4; i++) {
        y[i] = y[i+1];
      }
      y[4] = y_next;
    }
    if(xory) {
      for(int i = 0; i<4; i++) {
        y[i] = y[i+1];
      }
    }
    else {
      for(int i = 0; i<4; i++) {
        x[i] = x[i+1];
      }
    }
    for(int i = 0; i<5; i++) {
    b[y[i]] |= (1<<(6-x[i])); // put the values into the byte array
  }
  
  draw(b);
  for(int i = 0; i<5; i++) {
    Serial.print(i);
    Serial.print(": (x, y): ");
    Serial.print(x[i]);Serial.print(", ");Serial.println(y[i]);
  }
  delay(150);
  Serial.println("stuff");
  }
}

