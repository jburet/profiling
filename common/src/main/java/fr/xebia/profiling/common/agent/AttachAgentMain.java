package fr.xebia.profiling.common.agent;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.internal.Lists;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AttachAgentMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttachAgentMain.class);

    public static void main(String... args) {

        CommandLine cl = new CommandLine();
        JCommander jc = new JCommander(cl, args);

        try {
            VirtualMachine vm = VirtualMachine.attach(cl.pid);
            vm.loadAgent("./log-agent.jar", null);
        } catch (AttachNotSupportedException e) {
            LOGGER.error("Cannot attach agent", e);
        } catch (IOException e) {
            LOGGER.error("Cannot attach agent", e);
        } catch (AgentInitializationException e) {
            LOGGER.error("Cannot init agent", e);
        } catch (AgentLoadException e) {
            LOGGER.error("Cannot load agent", e);
        }
    }

    static class CommandLine {
        @Parameter
        public List<String> parameters = new ArrayList<String>();

        @Parameter(names = {"-p", "-pid"}, description = "PID of process to attach")
        public String pid;

    }

}
