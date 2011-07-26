namespace java fr.xebia.log.transport.thrift

// method log structure
struct MethodExecution {
    // agent id
	1: string clusterId,
	// agent id
	2: string serverId,
	// agent id
	3: string agentId,
	// thread name
	4: string threadName,
	// correlation id
	5: string threadId,
	// classname
	6: string className,
	// method name
	7: string methodName,
	// parameter
	8: map<string,string> parameters,
	// return value TODO change to binary
	9: optional string returnValue,
	// exception TODO change to binary
	10: optional string throwableType,
	// execution time
	11: i64 executionTimeInNano,
}


service Log {

	oneway void log(1:MethodExecution me)

}