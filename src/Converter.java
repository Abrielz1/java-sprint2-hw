import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Converter {
    HashMap<Integer, ArrayList<MonthData>>monthToYearData = new HashMap<>();
    ArrayList<CountMonth> countMonth;
    ArrayList<CountYear> countYear;
    ArrayList<MonthData>months;
    ArrayList<YearData> year;

    void convYear() {
        String content = readFileContentsOrNull("resources/y.2021.csv");
        String[] lines = content.split("\r?\n");
        year = new ArrayList<>();

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];
            String[] parts = line.split(",");
            String month = (parts[0]);
            int amount = Integer.parseInt(parts[1]);
            boolean isExpense = Boolean.parseBoolean(parts[2]);
            year.add(new YearData(month, amount, isExpense));
            }
        yearlyReport();
    }

    void yearlyReport() {

        countMonth = new ArrayList<>();
        for(Integer data : monthToYearData.keySet()) {
            int incomeMonth = 0;
            int expensesMonth = 0;
            for(MonthData set : monthToYearData.get(data)) {
                if (set.isExpense) {
                    expensesMonth += set.quantity * set.sumOfOne;
                } else {
                    incomeMonth += set.quantity * set.sumOfOne;
                }
            }
            countMonth.add(new CountMonth(incomeMonth, expensesMonth));
        }
        countYear = new ArrayList<CountYear>();
        for(YearData i : year) {

            int incomeYear = 0;
            int expensesYear= 0;
            for(YearData j : year) {

                if(j.month.equals(i.month) && !j.isExpense) {
                    incomeYear = j.amount;
                } else if (j.month.equals(i.month) && j.isExpense) {
                    expensesYear = j.amount;
                }
            }
            countYear.add(new CountYear(incomeYear, expensesYear));
        }
        for(int i = 0; i < countYear.size(); i++) {
            if(i%1 == 0) countYear.remove(i);
        }
    }

    public void converterMonth(){        // рубим месяцы на части...

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
             if (content.isExpense | finExpense < tempExpense){
                 //System.out.println(a + " True" + " не ложь (True), следовательно - расход," + " количество " +  content.quantity + "," + " стоимость " + content.sumOfOne); //отладка
                 tempExpense = content.quantity * content.sumOfOne;
                 finExpense = tempExpense;
                 Expense = content.itemName;
                 //System.out.println(content.quantity + " * " + content.sumOfOne + " = " + tempExpense); //отладка
             }  if (!content.isExpense | (finProfit < tempProfit)) {
                 //System.out.println(a + " False" + " ложь (False), следовательно - доход," + " количество " + content.quantity + "," + " стоимость " + content.sumOfOne);
                 tempProfit = content.quantity * content.sumOfOne;
                 //System.out.println(content.quantity + " * " + content.sumOfOne + "  =  " + tempProfit); //отладка
                 finProfit = tempProfit;
                 Profit = content.itemName;
                 //  System.out.println("Доход " + " finProfit " + finProfit + " tempProfit " + tempProfit + " Profit " + Profit);
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
                //System.out.println("не ложь - расход " + content.amount);
                expence = (content.amount);
                sumExpense[a++] = expence;
            } else {
                //System.out.println("ложь - доход " + content.amount);
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

    void grandFinal() {
        for(int i = 0; i < countMonth.size(); i++) {
            if(countMonth.get(i).income != countMonth.get(i).income && countMonth.get(i).expenses != countMonth.get(i).expenses) {
                System.out.println("В месяце " + (i+1) + " ошибка");
            }
        }
        System.out.println("Ошибок нет" + "\n");
    }



    private String readFileContentsOrNull(String path)  {
        try {
            return Files.readString(Path.of(path));
        }           catch (IOException e) {
            System.out.println("Невозможно прочитать файл с месячным отчётом. Возможно, файл не находится в нужной директории.");
            return null;
        }
    }
}

