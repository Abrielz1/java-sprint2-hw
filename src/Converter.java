import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Converter {
    HashMap<Integer, ArrayList<MonthData>>monthToYearData = new HashMap<>();
   // HashMap<Integer, ArrayList<CountYear>>yearCount =  new HashMap<>();
   // ArrayList<CountMonth> countMonth = new ArrayList<>();
   // ArrayList<CountYear> countYear = new ArrayList<>();
    ArrayList<YearData> year = new ArrayList<>();

    void convYear() {
        String content = readFileContentsOrNull("resources/y.2021.csv");
        String[] lines = content.split("\r?\n");
        year = new ArrayList<>();

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            String[] parts = line.split(",");
            int month = Integer.parseInt(parts[0]);
            int amount = Integer.parseInt(parts[1]);
            boolean isExpense = Boolean.parseBoolean(parts[2]);
            year.add(new YearData(month, amount, isExpense));
            }
        grandFinal();
    }

    public void converterMonth(){        // рубим месяцы на части...
        ArrayList<MonthData>months;
        for (int i = 1; i < 4; i++) {
            String content = readFileContentsOrNull("resources/m.20210" + i + ".csv");
            String[] lines = content.split("\r?\n");
            months = new ArrayList<>();
            for(int j = 1; j < lines.length; j++){
                String line = lines[j];
                String[] parts = line.split(",");
                String itemName = parts[0];
                boolean isExpense = Boolean.parseBoolean(parts[1]);
                int quantity = Integer.parseInt(parts[2]);
                int sumOfOne = Integer.parseInt(parts[3]);
                MonthData monthData = new MonthData(itemName, isExpense, quantity, sumOfOne);
                months.add(monthData);
            }
            monthToYearData.put(i, months);
        }
    }

    void grandFinal() {
        int[] monthStats;
        ArrayList<int[]> profitAndExpense = new ArrayList<>();
        for (Integer values : monthToYearData.keySet()) {
            int profitAll = 0;
            int expenseAll = 0;
            for (MonthData  content  : monthToYearData.get(values)) {
                if (content.isExpense) {
                    expenseAll += content.quantity * content.sumOfOne;
                } else profitAll += content.quantity * content.sumOfOne;
            }
            monthStats = new int[2];
            monthStats[0] = expenseAll;
            monthStats[1] = profitAll;
            profitAndExpense.add(monthStats);
        }

        for (YearData content : year)  {
                if (content.isExpense) {
                    if (content.month == 1) {
                        int[] month = profitAndExpense.get(0);
                        int januaryExpense = month[0];
                        if (!(januaryExpense == content.amount)) {
                            System.out.println("В отчетах за январь месяц допущена ошибка");
                        }
                    } else if (content.month == 2) {
                        int[] month = profitAndExpense.get(1);
                        int FebruaryExpense = month[0];
                        if (!(FebruaryExpense == content.amount)) {
                            System.out.println("В отчетах за февраль месяц допущена ошибка");
                        }
                    } else if (content.month == 3) {
                        int[] month = profitAndExpense.get(2);
                        int MarchExpense = month[0];
                        if (!(MarchExpense == content.amount)) {
                            System.out.println("В отчетах за март месяц допущена ошибка");
                        }
                    }
                } else {
                    if (content.month == 1) {
                        int[] month = profitAndExpense.get(0);
                        int januaryProfit = month[1];
                        if (!(januaryProfit == content.amount)) {
                            System.out.println("В отчетах за январь месяц допущена ошибка");
                        }
                    } else if (content.month == 2) {
                        int[] month = profitAndExpense.get(1);
                        int FebruaryProfit = month[1];
                        if (!(FebruaryProfit == content.amount)) {
                            System.out.println("В отчетах за февраль месяц допущена ошибка");
                        }
                    } else if (content.month == 3) {
                        int[] month = profitAndExpense.get(2);
                        int MarchProfit = month[1];
                        if (!(MarchProfit == content.amount)) {
                            System.out.println("В отчетах за март месяц допущена ошибка");
                        }
                    }
                }

        }
        System.out.println("Операция сверки завершина\n");
    }




    void monthDataPrint() {
  int a = 1; // стартовый месяц, итератор
     for (Integer values : monthToYearData.keySet()) {

         int tempProfit = 0;
         int tempExpense = 0;
         int finExpense = 0;
         int finProfit = 0;
         String Profit = "";
         String Expense = "";
         for (MonthData  content  : monthToYearData.get(values)) {

             if (content.isExpense) {
                 tempExpense = content.quantity * content.sumOfOne;
             }  else { tempProfit = content.quantity * content.sumOfOne; }
             if (finExpense < tempExpense) {
                 finExpense = tempExpense;
                 Expense = content.itemName;
             }
             if (finProfit < tempProfit) {
                 finProfit = tempProfit;
                 Profit = content.itemName;
             }
        }
         System.out.println("Максимальные доходы за " + a + " месяц " + finProfit + " " + Profit);
         System.out.println("Максимальные расходы за " + a + " месяц " + finExpense+ " " + Expense);
          a++;
     }
 }

    void yearDataPrint() { // итерации переименованы, что бы не запутаться я в курсе, что было в теле цикла, там и остаётся.
        int ourProfit = 0;
        int a = 0;
        int b = 0;
        int ourExpense = 0;
        int profit = 0;
        int expence = 0;
        int[]sumExpense = new int[3];
        int[]sumProfit = new int[3];
        System.out.println("Отчет за 2021 г.");
        for (YearData content : year) {
            if (content.isExpense){
                expence = (content.amount);
                sumExpense[a++] = expence;
            } else {
                profit = (content.amount);
                sumProfit[b++] = profit;
            }
        }
        for (int z = 1; z < 4; z++) {
            System.out.println("Прибыль за " + z + " месяц составил " + (sumProfit[z - 1] - sumExpense[z - 1]));

        }
        for (int k = 0; k < sumProfit.length; k++) {
            ourProfit+=sumProfit[k];
        }

        for (int h = 0; h < sumExpense.length; h++) {
            ourExpense += sumExpense[h];
        }
        System.out.println("Медианный доход за все месяцы " + ourProfit/12);
        System.out.println("Медианный расход за все месяцы " + ourExpense/12);

    }

 /*   void grandFinal() {
        if(countMonth.isEmpty()) {
            System.out.println("Данные отсутствуют");
        } else System.out.println("Данные присутствуют");
        for(int i = 0; i < countMonth.size(); i++) {
            if(countYear.get(i).income != countMonth.get(i).income || countYear.get(i).expenses != countMonth.get(i).expenses) {
                System.out.println("В месяце " + (i+1) + " ошибка");
            } else  System.out.println("Ошибок нет" + "\n");
 }   } */

     //надо доработать

    private String readFileContentsOrNull(String path)  {
        try {
            return Files.readString(Path.of(path));
        }           catch (IOException e) {
            System.out.println("Невозможно прочитать файл с месячным отчётом. Возможно, файл не находится в нужной директории.");
            return null;
        }
    }
}

