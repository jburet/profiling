/**
 * Autogenerated by Thrift
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 */
package fr.xebia.log.transport.thrift;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

  public interface Iface {

    public void log(MethodExecution me) throws org.apache.thrift.TException;

  }

  public interface AsyncIface {

    public void log(MethodExecution me, org.apache.thrift.async.AsyncMethodCallback<AsyncClient.log_call> resultHandler) throws org.apache.thrift.TException;

  }

  public static class Client implements org.apache.thrift.TServiceClient, Iface {
    public static class Factory implements org.apache.thrift.TServiceClientFactory<Client> {
      public Factory() {}
      public Client getClient(org.apache.thrift.protocol.TProtocol prot) {
        return new Client(prot);
      }
      public Client getClient(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) {
        return new Client(iprot, oprot);
      }
    }

    public Client(org.apache.thrift.protocol.TProtocol prot)
    {
      this(prot, prot);
    }

    public Client(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot)
    {
      iprot_ = iprot;
      oprot_ = oprot;
    }

    protected org.apache.thrift.protocol.TProtocol iprot_;
    protected org.apache.thrift.protocol.TProtocol oprot_;

    protected int seqid_;

    public org.apache.thrift.protocol.TProtocol getInputProtocol()
    {
      return this.iprot_;
    }

    public org.apache.thrift.protocol.TProtocol getOutputProtocol()
    {
      return this.oprot_;
    }

    public void log(MethodExecution me) throws org.apache.thrift.TException
    {
      send_log(me);
    }

    public void send_log(MethodExecution me) throws org.apache.thrift.TException
    {
      oprot_.writeMessageBegin(new org.apache.thrift.protocol.TMessage("log", org.apache.thrift.protocol.TMessageType.CALL, ++seqid_));
      log_args args = new log_args();
      args.setMe(me);
      args.write(oprot_);
      oprot_.writeMessageEnd();
      oprot_.getTransport().flush();
    }

  }
  public static class AsyncClient extends org.apache.thrift.async.TAsyncClient implements AsyncIface {
    public static class Factory implements org.apache.thrift.async.TAsyncClientFactory<AsyncClient> {
      private org.apache.thrift.async.TAsyncClientManager clientManager;
      private org.apache.thrift.protocol.TProtocolFactory protocolFactory;
      public Factory(org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.protocol.TProtocolFactory protocolFactory) {
        this.clientManager = clientManager;
        this.protocolFactory = protocolFactory;
      }
      public AsyncClient getAsyncClient(org.apache.thrift.transport.TNonblockingTransport transport) {
        return new AsyncClient(protocolFactory, clientManager, transport);
      }
    }

    public AsyncClient(org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.async.TAsyncClientManager clientManager, org.apache.thrift.transport.TNonblockingTransport transport) {
      super(protocolFactory, clientManager, transport);
    }

    public void log(MethodExecution me, org.apache.thrift.async.AsyncMethodCallback<log_call> resultHandler) throws org.apache.thrift.TException {
      checkReady();
      log_call method_call = new log_call(me, resultHandler, this, protocolFactory, transport);
      this.currentMethod = method_call;
      manager.call(method_call);
    }

    public static class log_call extends org.apache.thrift.async.TAsyncMethodCall {
      private MethodExecution me;
      public log_call(MethodExecution me, org.apache.thrift.async.AsyncMethodCallback<log_call> resultHandler, org.apache.thrift.async.TAsyncClient client, org.apache.thrift.protocol.TProtocolFactory protocolFactory, org.apache.thrift.transport.TNonblockingTransport transport) throws org.apache.thrift.TException {
        super(client, protocolFactory, transport, resultHandler, true);
        this.me = me;
      }

      public void write_args(org.apache.thrift.protocol.TProtocol prot) throws org.apache.thrift.TException {
        prot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("log", org.apache.thrift.protocol.TMessageType.CALL, 0));
        log_args args = new log_args();
        args.setMe(me);
        args.write(prot);
        prot.writeMessageEnd();
      }

      public void getResult() throws org.apache.thrift.TException {
        if (getState() != org.apache.thrift.async.TAsyncMethodCall.State.RESPONSE_READ) {
          throw new IllegalStateException("Method call not finished!");
        }
        org.apache.thrift.transport.TMemoryInputTransport memoryTransport = new org.apache.thrift.transport.TMemoryInputTransport(getFrameBuffer().array());
        org.apache.thrift.protocol.TProtocol prot = client.getProtocolFactory().getProtocol(memoryTransport);
      }
    }

  }

  public static class Processor implements org.apache.thrift.TProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class.getName());
    public Processor(Iface iface)
    {
      iface_ = iface;
      processMap_.put("log", new log());
    }

    protected static interface ProcessFunction {
      public void process(int seqid, org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException;
    }

    private Iface iface_;
    protected final HashMap<String,ProcessFunction> processMap_ = new HashMap<String,ProcessFunction>();

    public boolean process(org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException
    {
      org.apache.thrift.protocol.TMessage msg = iprot.readMessageBegin();
      ProcessFunction fn = processMap_.get(msg.name);
      if (fn == null) {
        org.apache.thrift.protocol.TProtocolUtil.skip(iprot, org.apache.thrift.protocol.TType.STRUCT);
        iprot.readMessageEnd();
        org.apache.thrift.TApplicationException x = new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.UNKNOWN_METHOD, "Invalid method name: '"+msg.name+"'");
        oprot.writeMessageBegin(new org.apache.thrift.protocol.TMessage(msg.name, org.apache.thrift.protocol.TMessageType.EXCEPTION, msg.seqid));
        x.write(oprot);
        oprot.writeMessageEnd();
        oprot.getTransport().flush();
        return true;
      }
      fn.process(msg.seqid, iprot, oprot);
      return true;
    }

    private class log implements ProcessFunction {
      public void process(int seqid, org.apache.thrift.protocol.TProtocol iprot, org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException
      {
        log_args args = new log_args();
        try {
          args.read(iprot);
        } catch (org.apache.thrift.protocol.TProtocolException e) {
          iprot.readMessageEnd();
          org.apache.thrift.TApplicationException x = new org.apache.thrift.TApplicationException(org.apache.thrift.TApplicationException.PROTOCOL_ERROR, e.getMessage());
          oprot.writeMessageBegin(new org.apache.thrift.protocol.TMessage("log", org.apache.thrift.protocol.TMessageType.EXCEPTION, seqid));
          x.write(oprot);
          oprot.writeMessageEnd();
          oprot.getTransport().flush();
          return;
        }
        iprot.readMessageEnd();
        iface_.log(args.me);
        return;
      }
    }

  }

  public static class log_args implements org.apache.thrift.TBase<log_args, log_args._Fields>, java.io.Serializable, Cloneable   {
    private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("log_args");

    private static final org.apache.thrift.protocol.TField ME_FIELD_DESC = new org.apache.thrift.protocol.TField("me", org.apache.thrift.protocol.TType.STRUCT, (short)1);

    public MethodExecution me;

    /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
    public enum _Fields implements org.apache.thrift.TFieldIdEnum {
      ME((short)1, "me");

      private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

      static {
        for (_Fields field : EnumSet.allOf(_Fields.class)) {
          byName.put(field.getFieldName(), field);
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, or null if its not found.
       */
      public static _Fields findByThriftId(int fieldId) {
        switch(fieldId) {
          case 1: // ME
            return ME;
          default:
            return null;
        }
      }

      /**
       * Find the _Fields constant that matches fieldId, throwing an exception
       * if it is not found.
       */
      public static _Fields findByThriftIdOrThrow(int fieldId) {
        _Fields fields = findByThriftId(fieldId);
        if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
        return fields;
      }

      /**
       * Find the _Fields constant that matches name, or null if its not found.
       */
      public static _Fields findByName(String name) {
        return byName.get(name);
      }

      private final short _thriftId;
      private final String _fieldName;

      _Fields(short thriftId, String fieldName) {
        _thriftId = thriftId;
        _fieldName = fieldName;
      }

      public short getThriftFieldId() {
        return _thriftId;
      }

      public String getFieldName() {
        return _fieldName;
      }
    }

    // isset id assignments

    public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
    static {
      Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
      tmpMap.put(_Fields.ME, new org.apache.thrift.meta_data.FieldMetaData("me", org.apache.thrift.TFieldRequirementType.DEFAULT, 
          new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, MethodExecution.class)));
      metaDataMap = Collections.unmodifiableMap(tmpMap);
      org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(log_args.class, metaDataMap);
    }

    public log_args() {
    }

    public log_args(
      MethodExecution me)
    {
      this();
      this.me = me;
    }

    /**
     * Performs a deep copy on <i>other</i>.
     */
    public log_args(log_args other) {
      if (other.isSetMe()) {
        this.me = new MethodExecution(other.me);
      }
    }

    public log_args deepCopy() {
      return new log_args(this);
    }

    @Override
    public void clear() {
      this.me = null;
    }

    public MethodExecution getMe() {
      return this.me;
    }

    public log_args setMe(MethodExecution me) {
      this.me = me;
      return this;
    }

    public void unsetMe() {
      this.me = null;
    }

    /** Returns true if field me is set (has been assigned a value) and false otherwise */
    public boolean isSetMe() {
      return this.me != null;
    }

    public void setMeIsSet(boolean value) {
      if (!value) {
        this.me = null;
      }
    }

    public void setFieldValue(_Fields field, Object value) {
      switch (field) {
      case ME:
        if (value == null) {
          unsetMe();
        } else {
          setMe((MethodExecution)value);
        }
        break;

      }
    }

    public Object getFieldValue(_Fields field) {
      switch (field) {
      case ME:
        return getMe();

      }
      throw new IllegalStateException();
    }

    /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
    public boolean isSet(_Fields field) {
      if (field == null) {
        throw new IllegalArgumentException();
      }

      switch (field) {
      case ME:
        return isSetMe();
      }
      throw new IllegalStateException();
    }

    @Override
    public boolean equals(Object that) {
      if (that == null)
        return false;
      if (that instanceof log_args)
        return this.equals((log_args)that);
      return false;
    }

    public boolean equals(log_args that) {
      if (that == null)
        return false;

      boolean this_present_me = true && this.isSetMe();
      boolean that_present_me = true && that.isSetMe();
      if (this_present_me || that_present_me) {
        if (!(this_present_me && that_present_me))
          return false;
        if (!this.me.equals(that.me))
          return false;
      }

      return true;
    }

    @Override
    public int hashCode() {
      return 0;
    }

    public int compareTo(log_args other) {
      if (!getClass().equals(other.getClass())) {
        return getClass().getName().compareTo(other.getClass().getName());
      }

      int lastComparison = 0;
      log_args typedOther = (log_args)other;

      lastComparison = Boolean.valueOf(isSetMe()).compareTo(typedOther.isSetMe());
      if (lastComparison != 0) {
        return lastComparison;
      }
      if (isSetMe()) {
        lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.me, typedOther.me);
        if (lastComparison != 0) {
          return lastComparison;
        }
      }
      return 0;
    }

    public _Fields fieldForId(int fieldId) {
      return _Fields.findByThriftId(fieldId);
    }

    public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField field;
      iprot.readStructBegin();
      while (true)
      {
        field = iprot.readFieldBegin();
        if (field.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (field.id) {
          case 1: // ME
            if (field.type == org.apache.thrift.protocol.TType.STRUCT) {
              this.me = new MethodExecution();
              this.me.read(iprot);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, field.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
      validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (this.me != null) {
        oprot.writeFieldBegin(ME_FIELD_DESC);
        this.me.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

    @Override
    public String toString() {
      StringBuilder sb = new StringBuilder("log_args(");
      boolean first = true;

      sb.append("me:");
      if (this.me == null) {
        sb.append("null");
      } else {
        sb.append(this.me);
      }
      first = false;
      sb.append(")");
      return sb.toString();
    }

    public void validate() throws org.apache.thrift.TException {
      // check for required fields
    }

    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
      try {
        write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
      try {
        read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
      } catch (org.apache.thrift.TException te) {
        throw new java.io.IOException(te);
      }
    }

  }

}
