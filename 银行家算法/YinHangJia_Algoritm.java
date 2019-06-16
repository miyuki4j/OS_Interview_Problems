package 银行家算法;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 *  数据结构说明:
 *  1.Max[i,j]=K,代表进程i 对Rj类资源的最大需求数
 *  2.Allocation[i,j]=K,代表为进程i 已分配的Rj类资源的数量
 *  3.Need[i,j]=K,代表为进程i 还需Rj类资源的数量才能运行完毕 Need[i,j]=Max[i,j]-Allocation[i,j]
 *  4.Avaliable[j]=K, 代表Rj类资源可利用的数量
 */

/**
 * 算法步骤：
 * 1.当某个进程来了请求资源向量:
 *      (1) 请求向量小于等于Avaliable向量
 *      (2) 请求向量小于等于Need矩阵
 *      通过以上两个条件，进行预分配
 * 2.进入安全检测算法 找到一个安全序列
 *      使用一个 Work=Avaliable finish
 *
 * 3.
 *
 *
 */
public class YinHangJia_Algoritm {
    private  final  int n = 5; //进程数
    private  final  int m = 3; //资源数
    private int Max[][];
    private int Allocation[][];
    private int Need[][];
    private int Avaliable[]; //系统可利用的资源
    public  void init(){
        Scanner scanner = new Scanner(System.in);
        Max = new int[n][m];
        System.out.println("请输入每个进程对每类资源的最大数量:");
        for (int i = 0; i <n ; i++) {
            for (int j = 0; j <m ; j++) {
                Max[i][j] = scanner.nextInt();
            }
            if(i<4)
                System.out.println("下一个进程:");
        }
        System.out.println("请输入每个进程对每类资源的已分配的数量:");
        Allocation = new int[n][m];
        scanner = new Scanner(System.in);
        for (int i = 0; i <n ; i++) {
            for (int j = 0; j <m ; j++) {
                Allocation[i][j] = scanner.nextInt();
            }
            if(i<4)
                System.out.println("下一个进程:");
        }
        System.out.println("请输入每一类资源的数量:");
        Avaliable = new int[m]; //m个资源的可利用数组
        scanner = new Scanner(System.in);
        for (int i = 0; i <m; i++) {
            Avaliable[i]=scanner.nextInt();
        }
        Need = new int[n][m];
        for (int i = 0; i <n ; i++) {
            for (int j = 0; j <m ; j++) {
                Need[i][j]= Max[i][j]-Allocation[i][j];
            }
        }
    }
    public boolean run(int request[]/*当前进程*/,int pro /*进程号*/,List list /*安全序列*/){
        //1.检查请求数量是否超过需求的数量
        for (int j = 0; j <request.length ; j++) {
            if(request[j]> Need[pro][j]){
                return  false;
            }
        }
        //2.检查请求的数量是否超过可利用的
        for (int i = 0; i <m ; i++) {
            if(request[i]>Avaliable[i])
                return false;
        }
        //3.试分配
        for (int i = 0; i <m ; i++) {
            Avaliable[i]-=request[i];
            Need[pro][i]-=request[i];
            Allocation[pro][i]+=request[i];
        }
        if(safe(list)){
            return true;
        }else{
            for (int i = 0; i <m ; i++) {
                Avaliable[i]+=request[i];
                Need[pro][i]+=request[i];
                Allocation[pro][i]-=request[i];
            }
            return false;
        }
    }
    public boolean safe(List list){
        //1.拷贝工作
        int [] Work = this.Avaliable; //可以利用资源的副本
        boolean []finish = new boolean[n];
        for (int i = 0; i <finish.length ; i++) {
            finish[i]=false;
        }
        //2.找到以finish[i] =false && Need[i][j]<=Work[j]
        int cnt_pro=-1; //当前找的进程号
        int cnt_count=0;// 累加到m时，即可以找的进程号
        boolean find=false; //是否找的
        do {
            for (int i = 0; i <n ; i++) {
                cnt_count=0; //计数器清0
                for (int j = 0; j <m ; j++) {
                    if(Need[i][j]<=Work[j])
                        cnt_count++;
                }
                if(cnt_count==m && !finish[i]){
                    cnt_pro=i;
                    find=true;
                    break;
                }
            }
            if(find){
                for (int i = 0; i <m ; i++) {
                    Work[i]+=Allocation[cnt_pro][i];
                }
                finish[cnt_pro]=true;
                list.add(cnt_pro);
            }
            if(!find)
                break;

        }while(!allFinish(finish));
        return allFinish(finish);
    }
    public  boolean allFinish(boolean finish[]){
        for (boolean flag:finish) {
            if(!flag)return false;
        }
        return  true;
    }
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        YinHangJia_Algoritm  o  = new YinHangJia_Algoritm();
        o.init();
        int req[] = {1,0,2}; //请求资源量
        int req_pro =1; //请求进程号
        List<Integer> list = new ArrayList<>();
        if(o.run(req,req_pro,list)){
            System.out.println("运行结果："+"可以安全运行");
            for (Integer seq : list) {
                System.out.print("process"+seq+"\t");
            }
        }else{
            System.out.println("运行结果："+"不可以安全运行");
        }
    }
}
