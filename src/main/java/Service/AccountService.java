package Service;
import Model.Account;
import DAO.AccountDao;
import java.util.List;

/*
 * The AccountService class provides mainly the business logic/validation logic for account
 */
public class AccountService {


    AccountDao accountDao;

    /*
     * Default constructor
     */
    public AccountService(){
        accountDao = new AccountDao();
    }

    /*
     * constructor allows for dependency injection of a custom AccountDao object
     */
    public AccountService(AccountDao t){
        this.accountDao = t;
    }

    /*
     * checkUser checks if a user exists in the database by their account_id
     * @param temp The account_id to be checked
     * @return boolean 
     */
    public boolean checkUser(int temp){
        if(!accountDao.checkPost(temp)){
            return false;
        }

        return true;
    }

    /*
     * addAccount adds a new user account to the database
     * @param account the account object containing user details to be added
     * @return Account will returns the newly created Account object or null if validation fails
     */
    public Account addAccount(Account account){
        if(accountDao.checkUserA(account.getUsername()) || account.getPassword().length() < 4 || account.getUsername() == ""){
            return null;
        }
        Account temp = accountDao.insertAccount(account);
        return temp;
    }

    /*
     * allAccount retrieves all accounts from the database
     * 
     * @return List<Account> a list of account objects representing all accounts in the database
     */
    public List<Account> allAccount(){
        List<Account> temp = accountDao.getAllAccount();

        return temp;
    }

    /*
     * loginAccount attempts to log in a user by checking their username and password
     *  @return Account the account object if login is successful or null if login fails as return from accountDao
     */
    public Account loginAccount(Account acc){
        Account fAccount = accountDao.getloginAccount(acc);

        return fAccount;
    }
}
