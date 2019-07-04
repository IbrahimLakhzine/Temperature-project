//#define F_CPU 16000000  //telling controller crystal frequency attached
//#include<stdio.h>
//#include<stdlib.h>
//#include<avr/io.h>
//#include <avr/interrupt.h>
//
//
//int binaryToDecimal(int num){
//  int binary_val, decimal_val = 0, base = 1, rem;
//
//    binary_val = num;
//    while (num > 0)
//    {
//        rem = num % 10;
//        decimal_val = decimal_val + rem * base;
//        num = num / 10 ;
//        base = base * 2;
//    }
//    printf("The Binary number is = %d \n", binary_val);
//    return decimal_val;
//}
//
//
//int main(int argc, char const *argv[])
//{
//  
//  DDRA &= ~(1<<PORTA0); // set A0 as input
//  ADMUX |=1<<MUX0;//input analog 
//  ADMUX |=1<<REFS0;//VCC
//  ADCSRA|=1<<ADPS2;//link between system and input
//  ADCSRA|=1<<ADEN;//enable conversion
//  ADCSRA|=1<<ADSC;//start conversion
//  ADCSRA|=1<<ADATE;//run the convertion continuously
//  ADCSRA|=1<<ADLAR;//when we found the result
//  ADCSRA|=1<<ADIE;//enable la routine
//  sei();//start routine
//  while(1){};
//  return 0;
//}
//
//
//
//
//ISR(ADC_vect){
//    int analogVal;
//    //float temp,v;
//    
//    analogVal=binaryToDecimal(ADC);
//    float temp=analogVal*(5.0/1024.0)*100.0;
// 
//    Serial.println(temp);
//    ADCSRA|=1<<ADSC;////restart conversion
//    sei();
//} 



// the setup routine runs once when you press reset:
int sensorValue = 0;
void setup() {
  // initialize serial communication at 9600 bits per second:
  Serial.begin(9600);
  sensorValue = analogRead(A0);
  delay(1000);
}

// the loop routine runs over and over again forever:
void loop() {
  // read the input on analog pin 0:
  sensorValue = analogRead(A0);
  float temp=sensorValue*(5.0/1023.0)*100.0;
  // print out the value you read:
  Serial.println(temp);
  delay(1000);        // delay in between reads for stability
}
