package Service;
import Model.Account;
import DAO.AccountDao;
import java.util.List;

public class AccountService {


    AccountDao accountDao;

    public AccountService(){
        accountDao = new AccountDao();
    }

    public AccountService(AccountDao t){
        this.accountDao = t;
    }

    public Account addAccount(Account account){
        if(accountDao.checkUserA(account.getUsername()) || account.getPassword().length() < 4 || account.getUsername() == ""){
            return null;
        }
        Account temp = accountDao.insertAccount(account);
        return temp;
    }

    public List<Account> allAccount(){
        List<Account> temp = accountDao.getAllAccount();

        return temp;
    }

    public Account loginAccount(Account acc){
        Account fAccount = accountDao.getloginAccount(acc);

        return fAccount;
    }
}
