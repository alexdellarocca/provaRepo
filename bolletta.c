# include <stdio.h>

//costo unitario di un kwh
#define KWH_UNIT_PRICE 10.0

int main (void)
{
	//INPUT VARIABLES	
	float monthly_canon;		//canone mensile
	char user_name [30];		//nome
	float previous_kwh;			//lettura precedente
	float actual_kwh;			//lettura attuale
	//OUTPUT VARIABLES
	float kwh_consumed;			//kwh da addebitare
	float total;				//totale bolletta

	printf("Insert your name\n");
	scanf("%s", user_name);

	printf("Insert monthly_canon\n");
	scanf("%f", &monthly_canon);

	printf("Insert previous kwhs\n");
	scanf("%f", &previous_kwh);
	
	printf("Insert actual kwhs\n");
	scanf("%f", &actual_kwh);
	
	if(actual_kwh < previous_kwh){
		printf("Your actual consume is minor than previous. You're data are incorrectly. You will be signaled to Police!\n");
		return 0;
	}

	kwh_consumed = actual_kwh - previous_kwh;						//kwh da addebitare
	total = monthly_canon + (kwh_consumed * KWH_UNIT_PRICE);		//totale bolletta

	printf("kwh_consumed %0.2f \n", kwh_consumed);					//%0.2 arrotondiamo alla seconda cifra decimale
	printf("total to pay %0.2f \n", total);							//%0.2 arrotondiamo alla seconda cifra decimale

	return 0;
}