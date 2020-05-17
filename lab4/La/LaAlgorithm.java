package lab4.La;

import lab4.Dane;
import lab4.Sensor;
import lab4.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class LaAlgorithm {
    Dane data;
    Random random;
    Statistics statistics;

    public LaAlgorithm(Dane data) {
        this.data = data;
        this.random=new Random(data.randomSeed);
    }

    public LaAlgorithm(Dane data, Statistics statistics) {
        this.statistics=statistics;
        this.data=data;
        this.random=new Random(data.randomSeed);
    }

    public List<List<Sensor>>getShedule()
    {
        Environment environment=new Environment(data.getListOfPoi(),data.getListOfSensors(),data.getPromien());
        List<List<Sensor>>result=new ArrayList<>();

        for(int i=1;i<data.laData.maxRunsNumber;i++)
        {
            Environment environmentNoDeadSensors=new Environment(environment);
            environmentNoDeadSensors.removeDeadSensors();
            Environment resultEnvironment=getBestSolutionForCurrentState(environmentNoDeadSensors,data.getListOfSensors());


            if(resultEnvironment!=null)//null mean that target coverage ratio hasn't been achieved
            {
                onlySolutionSensorsRemainOn(environment, resultEnvironment);

                result.add(environment.getSoulution(data.getListOfSensors()));
            }else {
                break;
            }

            environment.eraseBattery();
            List<Sensor>test=resultEnvironment.getSoulution(resultEnvironment.sensorsList);
            List<Sensor>withBattery=environment.sensorsList.stream().filter(x->x.getBateriaPojemnosc()!=0).collect(Collectors.toList());

        }
        return result;
    }

    private void onlySolutionSensorsRemainOn(Environment environment, Environment resultEnvironment) {
        List<Sensor> solution=resultEnvironment.getSoulution(resultEnvironment.sensorsList);
        environment.offAllSensors();
        environment.setSensorsStatesAccordingToList(solution);
    }

    /**
     * @return null mean that target coverage ratio hasn't been achieved
     * @param environment
     * @param listOfSensors
     */
    private Environment getBestSolutionForCurrentState(Environment environment, List<Sensor> listOfSensors) {

        int C_u=0;
        environment.resetSumC();

        initMemory(environment);
        environment.setRandomSensorsStatesKAndReadyToShare(random,data.laData.probSensorOn,data.laData.maxK,data.laData.probReadyToShare);
        List<List<Sensor>>runStatistics=new ArrayList<>();
        for(int i=0;i<data.laData.maxIterationsNumber;i++)
        {
            C_u++;
            if(C_u==data.laData.u) {
                environment.makeStrategyUSwap(data.laData.isRTSPlusStrategy);
            }
            else
                environment.setSensorsStatesAccordingToBestStrategyInMemory(data.laData.epslion,random);
                
            environment.discontReward(data);
            runStatistics.add(Utils.cloneList(environment.sensorsList));



        }
        List<Sensor>on=environment.sensorsList.stream().filter(x->x.getStan()==1).collect(Collectors.toList());
        List<Sensor>hasBattery=environment.sensorsList.stream().filter(x->x.getBateriaPojemnosc()>0).collect(Collectors.toList());
        double rate=environment.getCoverageRate();
        statistics.getRunsStateList().add(runStatistics);
        if(environment.getCoverageRate()<data.getQ())
        {
            return null;
        }
        return environment;
    }



    public void initMemory(Environment environment) {
//        Environment temp_environment=new Environment(environment,data.getPromien());
        environment.setRandomSensorsStatesKAndReadyToShare(random,data.laData.probSensorOn,data.laData.maxK, data.laData.probReadyToShare);
        for(int i=0;i<data.laData.h;i++)
        {
            environment.setNewStateAccordingToRandomStrategy(random,data.laData);
            environment.discontReward(data);
        }

    }


}
