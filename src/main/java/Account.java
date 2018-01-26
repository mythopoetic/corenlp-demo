public class Account {

    private String accountHolder;
    private int accountNumber;
    private int accountBalance;

    public Account(String accountHolder, int accountNumber, int accountBalance) {
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
    }

    public String getAccountHolder() {
        return this.accountHolder;
    }

    public int getAccountNumber() {
        return this.accountNumber;
    }

    public int getAccountBalance() {
        return this.accountBalance;
    }

}
