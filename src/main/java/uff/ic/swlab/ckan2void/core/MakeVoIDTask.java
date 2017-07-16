package uff.ic.swlab.ckan2void.core;

import java.util.concurrent.Callable;
import org.apache.jena.rdf.model.Model;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import uff.ic.swlab.ckan2void.adapter.Dataset;
import uff.ic.swlab.ckan2void.helper.VoIDHelper;
import uff.ic.swlab.ckan2void.util.Config;
import uff.ic.swlab.ckan2void.util.Executor;

public class MakeVoIDTask implements Runnable {

    private final Dataset dataset;
    private final String graphUri;
    private final String graphDerefUri;

    private static final InstanceCounter INSTANCE_COUNTER = new InstanceCounter(Config.TASK_INSTANCES);

    private static class InstanceCounter {

        private int instances;

        public InstanceCounter(int instances) {
            this.instances = instances;
        }

        public synchronized void startInstance() {
            while (true)
                if (instances > 0) {
                    instances--;
                    break;
                } else
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                    }
        }

        public synchronized void finilizeInstance() {
            instances++;
            notifyAll();
        }
    }

    public MakeVoIDTask(Dataset dataset, String graphURI) {
        INSTANCE_COUNTER.startInstance();
        this.dataset = dataset;
        this.graphUri = graphURI;
        this.graphDerefUri = Config.HOST.getQuadsURL(Config.FUSEKI_DATASET) + "?graph=" + graphUri;
    }

    @Override
    public final void run() {
        runTask();
        INSTANCE_COUNTER.finilizeInstance();
    }

    private void runTask() {
        class Result {

            Model _void;
            Model _voidComp;

            Result(Model _void, Model _voidComp) {
                this._void = _void;
                this._voidComp = _voidComp;
            }
        }

        try {
            String[] urls = dataset.getURLs(dataset);
            String[] sparqlEndPoints = dataset.getSparqlEndPoints();

            Callable<Result> task = () -> {
                Model _void = dataset.toVoid(graphDerefUri);
                Model _voidComp = VoIDHelper.getContent(urls, sparqlEndPoints, dataset.getUri());
                return new Result(_void, _voidComp);
            };
            Result voids = Executor.execute(task, "Make void of " + dataset.getUri(), Config.TASK_TIMEOUT);
            Config.HOST.saveVoid(voids._void, voids._voidComp, dataset.getUri(), graphUri);

        } catch (Throwable e) {
            Logger.getLogger("error").log(Level.ERROR, String.format("Task failure (<%1$s>). Msg: %2$s", graphUri, e.getMessage()));
        }

    }

}
