namespace java fr.xebia.log.transport.thrift

// method log structure
struct MethodExecution {
	// agent id
	1: string agentId,
	// thread name
	2: string threadName,
	// correlation id
	3: string correlationId,
	// classname
	4: string className,
	// method name
	5: string methodName,
	// parameter
	6: map<string,binary> parameters,
	// return value TODO change to binary
	7: optional string returnValue,
	// exception TODO change to binary
	8: optional string throwableType,
	// execution time
	9: i64 executionTimeInNano,
}


service Log {

	oneway void log(1:MethodExecution me)

}