#include <SoftwareSerial.h>
#include <Servo.h>

#define ESPport Serial
#define s2  6
#define s3  5
#define out 7
#define servoPin 11
#define motorPin 13
#define rledPin 14
#define gledPin 15

byte red = 0;
byte green = 0;
byte blue = 0;
byte rSt = 0;
byte gSt = 0;
byte bSt = 0;
byte ySt = 0;
Servo servo;

bool FAIL_8266 = false;

byte mDetected = 0;
byte rDetected = 0;
byte gDetected = 0;
byte bDetected = 0;
byte yDetected = 0;
#define BUFFER_SIZE 128
char buffer[BUFFER_SIZE];
 
void setup() 
{ 
  pinMode(rledPin, OUTPUT);
  pinMode(gledPin, OUTPUT);
  pinMode(motorPin, OUTPUT);
  pinMode(s2, OUTPUT);
  pinMode(s3, OUTPUT);
  servo.attach(servoPin);
  pinMode(out, INPUT);   
  Serial.begin(9600);
  ESPport.begin(115200);

  do{
    ESPport.println("AT");
    delay(1000);
    if(ESPport.find((char*)"OK"))
    {
      Serial.println("Modue is ready");
      delay(100);
      clearSerialBuffer();
      Serial.println("RESET 3,5 sek");
      Serial.println(GetResponse("AT+RST",3400)); // перезагрузка ESP
      Serial.println(GetResponse("AT+CWMODE=1",300)); // режим клиента   
      connectWiFi("Parol","1029384756"); // подключаемся к домашнему роутеру (имя точки, пароль) 
      Serial.println(GetResponse("AT+CIPMODE=0",300)); // сквозной режим передачи данных. 
      Serial.println(GetResponse("AT+CIPMUX=1",300)); // multiple connection.
      Serial.print("Start TCP-server: ");
      Serial.println(GetResponse("AT+CIPSERVER=1,88", 300)); // запускаем ТСР-сервер на 88-ом порту
      Serial.println(GetResponse("AT+CIPSTO=2", 300)); // таймаут сервера 2 сек
      Serial.println(GetResponse("AT+CIFSR", 300)); // узнаём адрес
      FAIL_8266 = false;
      }
     else {
      Serial.println("Module no resp");
      delay(500);
      FAIL_8266=true;
      }
    }while(FAIL_8266);

  ESPport.setTimeout(100);
  servo.write(10);
  digitalWrite(motorPin, LOW);
}

void loop() 
{
  servo.write(10);
  if(mDetected == 1){
    digitalWrite(gledPin, HIGH);
    digitalWrite(rledPin, LOW);
    color();
    } else{
    digitalWrite(rledPin, HIGH);
    digitalWrite(gledPin, LOW);
      }
  serva();
  //Serial.print(" RED: " + String(red));
  //Serial.print(" GREEN: " + String(green));
  //Serial.println(" BLUE: " + String(blue));

  int ch_id, packet_len;
  char *pb;  
  ESPport.readBytesUntil('\n', buffer, BUFFER_SIZE);
  
  if(strncmp(buffer, "+IPD,", 5)==0)
  {
   sscanf(buffer+5, "%d,%d", &ch_id, &packet_len);
   if (packet_len > 0)
    {
      pb = buffer+5;
      while(*pb!=':') pb++;
      pb++;
      if(strncmp(pb, "GET /a ", 6) == 0)
       {
        Serial.println(buffer);
        Serial.print("get led from ch :");
        Serial.println(ch_id);
        delay(100);
        clearSerialBuffer(); 
        if(mDetected == 0)
          {
            mDetected = 1;
          }
        
        else 
          {
            mDetected = 0;
          } 

        otvet_klienty(ch_id);
       } 
       
      if(strncmp(pb, "GET /b ", 6) == 0)
       {
        Serial.println(buffer);
        Serial.print("get led from ch :");
        Serial.println(ch_id);
        delay(100);
        clearSerialBuffer(); 
        if(rDetected == 0)
          {
            rDetected = 1;
          }
        
        else 
          {
            rDetected = 0;
          } 
        
        otvet_klienty(ch_id);
       } 

      if(strncmp(pb, "GET /c ", 6) == 0)
       {
        Serial.println(buffer);
        Serial.print("get led from ch :");
        Serial.println(ch_id);
        delay(100);
        clearSerialBuffer(); 
        if(gDetected == 0)
          {
            gDetected = 1;
          }
        
        else 
          {
            gDetected = 0;
          } 
        
        otvet_klienty(ch_id);
       } 
      if(strncmp(pb, "GET /d ", 6) == 0)
       {
        Serial.println(buffer);
        Serial.print("get led from ch :");
        Serial.println(ch_id);
        delay(100);
        clearSerialBuffer(); 
        if(bDetected == 0)
          {
            bDetected = 1;
          }
        
        else 
          {
            bDetected = 0;
          } 
        
        otvet_klienty(ch_id);
       } 
      if(strncmp(pb, "GET /e ", 6) == 0)
       {
                Serial.println(buffer);
        Serial.print("get led from ch :");
        Serial.println(ch_id);
        delay(100);
        clearSerialBuffer(); 
        if(yDetected == 0)
          {
            yDetected = 1;
          }
        
        else 
          {
            yDetected = 0;
          } 
        
        otvet_klienty(ch_id);
       } 

      if(strncmp(pb, "GET / ", 6) == 0)
       {
        Serial.println(buffer);
        Serial.print("get led from ch :");
        Serial.println(ch_id);
        delay(100);
        clearSerialBuffer(); 

        otvet_klienty(ch_id);
       } 
    }
  }
  clearBuffer();
}

void otvet_klienty(int ch_id) 
{                     
  String Header; 
 
  Header =  "HTTP/1.1 200 OK\r\n";  
  Header += "Content-Type: text/html\r\n";
  Header += "Connection: close\r\n";  
  
  String Content;

  Content = "" + intBool(mDetected) + intBool(rDetected) + intBool(gDetected) + intBool(bDetected) + intBool(yDetected) ;
  Serial.println("" + intBool(mDetected) + intBool(rDetected) + intBool(gDetected) + intBool(bDetected) + intBool(yDetected));

  
  Header += "Content-Length: ";
  Header += (int)(Content.length());
  Header += "\r\n\r\n";
  
  ESPport.print("AT+CIPSEND=");
  ESPport.print(ch_id);
  ESPport.print(",");
  ESPport.println(Header.length()+Content.length());
  delay(20);

  if(ESPport.find(">"))
    {
      ESPport.print(Header);
      ESPport.print(Content);
      delay(110);
    }
}

String GetResponse(String AT_Command, int wait)
{
  String tmpData;
  
  ESPport.println(AT_Command);
  delay(wait);
  while (ESPport.available() >0 )  
   {
    char c = ESPport.read();
    tmpData += c;
    
    if ( tmpData.indexOf(AT_Command) > -1 )         
      tmpData = "";
    else
      tmpData.trim();       
          
   }
  return tmpData;
}

void clearSerialBuffer(void) 
{
       while ( ESPport.available() > 0 ) 
       {
         ESPport.read();
       }
}

void clearBuffer(void) {
       for (int i =0;i<BUFFER_SIZE;i++ ) 
       {
         buffer[i]=0;
       }
}
     
boolean connectWiFi(String NetworkSSID,String NetworkPASS) 
{
  String cmd = "AT+CWJAP=\"";
  cmd += NetworkSSID;
  cmd += "\",\"";
  cmd += NetworkPASS;
  cmd += "\"";
  Serial.println(cmd); 
  Serial.println(GetResponse(cmd,10000));
}

void color() {
 // если 2 и 3 порты отключить, то получим значение красного цвета
 digitalWrite(s2, LOW);
 digitalWrite(s3, LOW);
 red = pulseIn(out, digitalRead(out) == HIGH ? LOW : HIGH);

 // если 3 порт включить, а 2 отключить, то получим синий цвет
 digitalWrite(s3, HIGH);
 blue = pulseIn(out, digitalRead(out) == HIGH ? LOW : HIGH);

 // если 2 включить, а 3 отключить, то получим зеленый цвет
 digitalWrite(s2, HIGH);
 green = pulseIn(out, digitalRead(out) == HIGH ? LOW : HIGH);



  if (red < 45 && green < 65){
    digitalWrite(motorPin, LOW);
    ++ySt;
    Serial.println(" THIS IS Жёлтый");
    delay(90);
    }
  else if(red < 60 && red < green && red < blue){
    digitalWrite(motorPin, LOW);
    ++rSt;
    Serial.println(" THIS IS КРАСНЫЙ");
    delay(90);
    }
  else if(green < 100 && green < red && green < blue){
    digitalWrite(motorPin, LOW);
    ++gSt;
    Serial.println(" THIS IS ЗЕЛЕНЫЙ");
    delay(90);
    }
  else if(blue < 80 && blue < red && blue < green){
    digitalWrite(motorPin, LOW);
    ++bSt;
    Serial.println(" THIS IS ГОЛУБОЙ");
    delay(90);
    }
  else{
    digitalWrite(motorPin, HIGH);
  }
}

void serva(){
  if (rSt == 4){
    if (rDetected == 1){
      servo.write(120);
      delay(300);
    }
    Serial.println(rSt);
    clearSt();
    }
  else if(gSt == 4){
    if (gDetected == 1){
      servo.write(120);
      delay(300);
    }
    Serial.println(gSt);
    clearSt();
    }
  else if(ySt == 4){
    if (bDetected == 1){
      servo.write(120);
      delay(300);
    }
    Serial.println(ySt);
    clearSt();
    }
  else if(bSt == 4){
    if (yDetected == 1){
      servo.write(120);
      delay(300);
    }
    Serial.println(bSt);
    clearSt();
    }
  }

void clearSt(){
  rSt = 0;
  ySt = 0;
  bSt = 0;
  gSt = 0;
}

String intBool(byte value){
  if(value == 1){
    return "1";
  } else {
    return "0";
  }
}