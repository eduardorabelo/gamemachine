
package io.gamemachine.messages;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.io.UnsupportedEncodingException;

import io.protostuff.ByteString;
import io.protostuff.GraphIOUtil;
import io.protostuff.Input;
import io.protostuff.Message;
import io.protostuff.Output;
import io.protostuff.ProtobufOutput;

import java.io.ByteArrayOutputStream;
import io.protostuff.JsonIOUtil;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;

import io.gamemachine.util.LocalLinkedBuffer;


import java.nio.charset.Charset;


import java.lang.reflect.Field;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSelection;
import akka.pattern.AskableActorSelection;

import akka.util.Timeout;
import java.util.concurrent.TimeUnit;


import io.gamemachine.core.ActorUtil;

import org.javalite.common.Convert;
import org.javalite.activejdbc.Model;
import io.protostuff.Schema;
import io.protostuff.UninitializedMessageException;

import io.gamemachine.core.PersistableMessage;


import io.gamemachine.objectdb.Cache;
import io.gamemachine.core.CacheUpdate;

@SuppressWarnings("unused")
public final class Entity implements Externalizable, Message<Entity>, Schema<Entity>, PersistableMessage{



    public static Schema<Entity> getSchema()
    {
        return DEFAULT_INSTANCE;
    }

    public static Entity getDefaultInstance()
    {
        return DEFAULT_INSTANCE;
    }

    static final Entity DEFAULT_INSTANCE = new Entity();
    static final String defaultScope = Entity.class.getSimpleName();

    			public Neighbors neighbors;
	    
        			public ChatMessage chatMessage;
	    
        			public ClientConnection clientConnection;
	    
        			public EchoTest echoTest;
	    
        			public String id;
	    
        			public Subscribe subscribe;
	    
        			public Publish publish;
	    
        			public ChatChannel chatChannel;
	    
        			public JoinChat joinChat;
	    
        			public LeaveChat leaveChat;
	    
        			public Unsubscribe unsubscribe;
	    
        			public ChatRegister chatRegister;
	    
        			public ChatChannels chatChannels;
	    
        			public ErrorMessage errorMessage;
	    
        			public NeighborsRequest neighborsRequest;
	    
        			public TrackEntity trackEntity;
	    
        			public GmVector3 vector3;
	    
        			public EntityList entityList;
	    
        			public Boolean published;
	    
        			public String entityType;
	    
        			public PlayerAuthenticated playerAuthenticated;
	    
        			public PlayerLogout playerLogout;
	    
        			public Boolean sendToPlayer;
	    
        			public Subscribers subscribers;
	    
        			public Boolean save;
	    
        			public ObjectdbGetResponse objectdbGetResponse;
	    
        			public NativeBytes nativeBytes;
	    
        			public ObjectdbGet objectdbGet;
	    
        			public JsonEntity jsonEntity;
	    
        			public String destination;
	    
        			public Boolean json;
	    
        			public String params;
	    
        			public ChatStatus chatStatus;
	    
        			public ChatBannedList chatBannedList;
	    
        			public ChatInvite chatInvite;
	    
        			public ClientManagerRegister clientManagerRegister;
	    
        			public ClientManagerUnregister clientManagerUnregister;
	    
        			public ClientEvent clientEvent;
	    
        			public ClientEvents clientEvents;
	    
        			public JsonStorage jsonStorage;
	    
        			public ClientManagerEvent clientManagerEvent;
	    
        			public Regions regions;
	    
        			public Boolean fastpath;
	    
        			public PoisonPill poisonPill;
	    
        			public String senderId;
	    
        			public AgentTrackData agentTrackData;
	    
        			public TeamMemberSkill teamMemberSkill;
	    
        			public TrackDataUpdate trackDataUpdate;
	    
        			public TrackDataResponse trackDataResponse;
	    
        			public GmMesh mesh;
	    
        			public PathData pathData;
	    
        			public ObjectdbStatus objectdbStatus;
	    
        			public GuildAction guildAction;
	    
        			public PvpGameMessage pvpGameMessage;
	    
        			public Player player;
	    
        			public TrackData trackData;
	    
        			public DynamicMessage dynamicMessage;
	    
        			public PlayerSkills playerSkills;
	    
        			public PlayerItems playerItems;
	    
        			public Players players;
	    
        			public TestObject testObject;
	    
        			public RpcMessage rpcMessage;
	    
        			public GameMessage gameMessage;
	    
        			public GameMessages gameMessages;
	    
        
	public static EntityCache cache() {
		return EntityCache.getInstance();
	}
	
	public static EntityStore store() {
		return EntityStore.getInstance();
	}


    public Entity()
    {
        
    }

	static class CacheRef {
	
		private final CacheUpdate cacheUpdate;
		private final String id;
		
		public CacheRef(CacheUpdate cacheUpdate, String id) {
			this.cacheUpdate = cacheUpdate;
			this.id = id;
		}
		
		public void send() {
			ActorSelection sel = ActorUtil.findLocalDistributed("cacheUpdateHandler", id);
			sel.tell(cacheUpdate,null);
		}
		
		public Entity result(int timeout) {
			ActorSelection sel = ActorUtil.findLocalDistributed("cacheUpdateHandler", id);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Future<Object> future = askable.ask(cacheUpdate, t);
			try {
				Await.result(future, t.duration());
				return cache().getCache().get(id);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	public static class EntityCache {

		private static HashMap<String, Field> cachefields = new HashMap<String, Field>();
		private static Cache<String, Entity> cache = new Cache<String, Entity>(120, 5000);
		
		private EntityCache() {
		}
		
		private static class LazyHolder {
			private static final EntityCache INSTANCE = new EntityCache();
		}
	
		public static EntityCache getInstance() {
			return LazyHolder.INSTANCE;
		}
		
	    public void init(int expiration, int size) {
			cache = new Cache<String, Entity>(expiration, size);
		}
	
		public Cache<String, Entity> getCache() {
			return cache;
		}
		
		public CacheRef setField(String id, String field, Object value) {
			return updateField(id, field, value, CacheUpdate.SET);
		}
		
		public CacheRef incrementField(String id, String field, Object value) {
			return updateField(id, field, value, CacheUpdate.INCREMENT);
		}
		
		public CacheRef decrementField(String id, String field, Object value) {
			return updateField(id, field, value, CacheUpdate.DECREMENT);
		}
		
		private CacheRef updateField(String id, String field, Object value, int updateType) {
			CacheUpdate cacheUpdate = new CacheUpdate(EntityCache.class, id, value, field, updateType);
			return new CacheRef(cacheUpdate,id);
		}
		
		public CacheRef set(Entity message) {
			CacheUpdate cacheUpdate = new CacheUpdate(EntityCache.class, message);
			return new CacheRef(cacheUpdate,message.id);
		}
	
		public Entity get(String id, int timeout) {
			Entity message = cache.get(id);
			if (message == null) {
				message = Entity.store().get(id, timeout);
			}
			return message;
		}
			
		public static Entity setFromUpdate(CacheUpdate cacheUpdate) throws IllegalArgumentException, IllegalAccessException  {
			Entity message = null;
			String field = cacheUpdate.getField();
			if (field == null) {
				message = (Entity) cacheUpdate.getMessage();
				if (message == null) {
					throw new RuntimeException("Attempt to store empty message in cache");
				}
				cache.set(message.id, message);
				Entity.store().set(message);
			} else {
				message = Entity.cache().get(cacheUpdate.getId(), 10);
				if (message == null) {
					throw new RuntimeException("Cannot set field on null message");
				}
				if (!cachefields.containsKey(field)) {
	            	try {
						cachefields.put(field, Entity.class.getField(field));
					} catch (NoSuchFieldException e) {
						throw new RuntimeException("No such field "+field);
					} catch (SecurityException e) {
						throw new RuntimeException("Security Exception accessing field "+field);
					}
	        	}
				Field f = cachefields.get(field);
				Class<?> klass = f.getType();
				if (cacheUpdate.getUpdateType() == CacheUpdate.SET) {
					f.set(message, klass.cast(cacheUpdate.getFieldValue()));
				} else {
					int updateType = cacheUpdate.getUpdateType();
					Object value = cacheUpdate.getFieldValue();
					if (klass == Integer.TYPE || klass == Integer.class) {
						Integer i;
						if (updateType == CacheUpdate.INCREMENT) {
							i = (Integer)f.get(message) + (Integer) value;
							f.set(message, klass.cast(i));
						} else if (updateType == CacheUpdate.DECREMENT) {
							i = (Integer)f.get(message) - (Integer) value;
							f.set(message, klass.cast(i));
						}
					} else if (klass == Long.TYPE || klass == Long.class) {
						Long i;
						if (updateType == CacheUpdate.INCREMENT) {
							i = (Long)f.get(message) + (Long) value;
							f.set(message, klass.cast(i));
						} else if (updateType == CacheUpdate.DECREMENT) {
							i = (Long)f.get(message) - (Long) value;
							f.set(message, klass.cast(i));
						}
					} else if (klass == Double.TYPE || klass == Double.class) {
						Double i;
						if (updateType == CacheUpdate.INCREMENT) {
							i = (Double)f.get(message) + (Double) value;
							f.set(message, klass.cast(i));
						} else if (updateType == CacheUpdate.DECREMENT) {
							i = (Double)f.get(message) - (Double) value;
							f.set(message, klass.cast(i));
						}
					} else if (klass == Float.TYPE || klass == Float.class) {
						Float i;
						if (updateType == CacheUpdate.INCREMENT) {
							i = (Float)f.get(message) + (Float) value;
							f.set(message, klass.cast(i));
						} else if (updateType == CacheUpdate.DECREMENT) {
							i = (Float)f.get(message) - (Float) value;
							f.set(message, klass.cast(i));
						}
					}
				}
				cache.set(message.id, message);
				Entity.store().set(message);
			}
			return message;
		}
	
	}
	
	public static class EntityStore {
	
		private EntityStore() {
		}
		
		private static class LazyHolder {
			private static final EntityStore INSTANCE = new EntityStore();
		}
	
		public static EntityStore getInstance() {
			return LazyHolder.INSTANCE;
		}
		
		public static String scopeId(String playerId, String id) {
    		return playerId + "##" + id;
    	}
    
	    public static String unscopeId(String id) {
	    	if (id.contains("##")) {
	    		String[] parts = id.split("##");
	        	return parts[1];
	    	} else {
	    		throw new RuntimeException("Expected "+id+" to contain ##");
	    	}
	    }
	    
	    public static String defaultScope() {
	    	return defaultScope;
	    }
		
	    public void set(Entity message) {
	    	set(defaultScope(),message);
		}
	    
	    public void delete(String id) {
	    	delete(defaultScope(),id);
	    }
	    
	    public Entity get(String id, int timeout) {
	    	return get(defaultScope(),id,timeout);
	    }
	    
	    public void set(String scope, Entity message) {
	    	Entity clone = message.clone();
			clone.id = scopeId(scope,message.id);
			ActorSelection sel = ActorUtil.findDistributed("object_store", clone.id);
			sel.tell(clone, null);
		}
			
		public void delete(String scope, String id) {
			String scopedId = scopeId(scope,id);
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			ObjectdbDel del = new ObjectdbDel().setEntityId(scopedId);
			sel.tell(del, null);
		}
			
		public Entity get(String scope, String id, int timeout) {
			String scopedId = scopeId(scope,id);
			ObjectdbGet get = new ObjectdbGet().setEntityId(scopedId).setKlass("Entity");
			ActorSelection sel = ActorUtil.findDistributed("object_store", scopedId);
			Timeout t = new Timeout(Duration.create(timeout, TimeUnit.MILLISECONDS));
			AskableActorSelection askable = new AskableActorSelection(sel);
			Entity message = null;
			Object result;
			Future<Object> future = askable.ask(get,t);
			try {
				result = Await.result(future, t.duration());
				if (result instanceof Entity) {
					message = (Entity)result;
				} else if (result instanceof ObjectdbStatus) {
					return null;
				}
			} catch (Exception e) {
				throw new RuntimeException("Operation timed out");
			}
			if (message == null) {
				return null;
			}
			message.id = unscopeId(message.id);
			return message;
		}
		
	}
	

	


	public ArrayList<String> componentNames() {
		ArrayList<String> names = new ArrayList<String>();
		  		  		  		  		  		  		  		  		  		  		  		  		  		  		  		if (this.hasGuildAction()) {
			names.add(this.guildAction.getClass().getSimpleName());
		}
	  		  		  		  		  		  		  		  		  		  		  		  		  		  		  		  		  		  		if (this.hasPvpGameMessage()) {
			names.add(this.pvpGameMessage.getClass().getSimpleName());
		}
	  		  		  		  		  		  		  		  		  		  		  		  		  		  		  		if (this.hasPlayer()) {
			names.add(this.player.getClass().getSimpleName());
		}
	  		  		if (this.hasTrackData()) {
			names.add(this.trackData.getClass().getSimpleName());
		}
	  		  		  		  		  		if (this.hasDynamicMessage()) {
			names.add(this.dynamicMessage.getClass().getSimpleName());
		}
	  		  		  		if (this.hasPlayerSkills()) {
			names.add(this.playerSkills.getClass().getSimpleName());
		}
	  		  		  		  		  		  		  		  		  		  		  		  		  		  		if (this.hasPlayerItems()) {
			names.add(this.playerItems.getClass().getSimpleName());
		}
	  		  		  		  		  		if (this.hasPlayers()) {
			names.add(this.players.getClass().getSimpleName());
		}
	  		  		if (this.hasTestObject()) {
			names.add(this.testObject.getClass().getSimpleName());
		}
	  		  		if (this.hasRpcMessage()) {
			names.add(this.rpcMessage.getClass().getSimpleName());
		}
	  		  		  		  		  		if (this.hasGameMessage()) {
			names.add(this.gameMessage.getClass().getSimpleName());
		}
	  		  		if (this.hasGameMessages()) {
			names.add(this.gameMessages.getClass().getSimpleName());
		}
	  		  		  		  		if (this.hasTrackDataUpdate()) {
			names.add(this.trackDataUpdate.getClass().getSimpleName());
		}
	  		  		if (this.hasTrackDataResponse()) {
			names.add(this.trackDataResponse.getClass().getSimpleName());
		}
	  		  		  		  		if (this.hasTeamMemberSkill()) {
			names.add(this.teamMemberSkill.getClass().getSimpleName());
		}
	  		  		  		  		  		  		if (this.hasAgentTrackData()) {
			names.add(this.agentTrackData.getClass().getSimpleName());
		}
	  		  		  		  		  		  		if (this.hasTrackEntity()) {
			names.add(this.trackEntity.getClass().getSimpleName());
		}
	  		  		if (this.hasPlayerLogout()) {
			names.add(this.playerLogout.getClass().getSimpleName());
		}
	  		  		  		  		if (this.hasPlayerAuthenticated()) {
			names.add(this.playerAuthenticated.getClass().getSimpleName());
		}
	  		  		if (this.hasErrorMessage()) {
			names.add(this.errorMessage.getClass().getSimpleName());
		}
	  		  		if (this.hasChatInvite()) {
			names.add(this.chatInvite.getClass().getSimpleName());
		}
	  		  		  		if (this.hasChatBannedList()) {
			names.add(this.chatBannedList.getClass().getSimpleName());
		}
	  		  		if (this.hasChatChannels()) {
			names.add(this.chatChannels.getClass().getSimpleName());
		}
	  		  		if (this.hasChatChannel()) {
			names.add(this.chatChannel.getClass().getSimpleName());
		}
	  		  		if (this.hasChatRegister()) {
			names.add(this.chatRegister.getClass().getSimpleName());
		}
	  		  		if (this.hasJoinChat()) {
			names.add(this.joinChat.getClass().getSimpleName());
		}
	  		  		if (this.hasLeaveChat()) {
			names.add(this.leaveChat.getClass().getSimpleName());
		}
	  		  		if (this.hasChatMessage()) {
			names.add(this.chatMessage.getClass().getSimpleName());
		}
	  		  		if (this.hasChatStatus()) {
			names.add(this.chatStatus.getClass().getSimpleName());
		}
	  		  		  		if (this.hasClientEvent()) {
			names.add(this.clientEvent.getClass().getSimpleName());
		}
	  		  		if (this.hasClientEvents()) {
			names.add(this.clientEvents.getClass().getSimpleName());
		}
	  		  		if (this.hasClientManagerUnregister()) {
			names.add(this.clientManagerUnregister.getClass().getSimpleName());
		}
	  		  		if (this.hasClientManagerRegister()) {
			names.add(this.clientManagerRegister.getClass().getSimpleName());
		}
	  		  		if (this.hasClientManagerEvent()) {
			names.add(this.clientManagerEvent.getClass().getSimpleName());
		}
	  		  		if (this.hasSubscribers()) {
			names.add(this.subscribers.getClass().getSimpleName());
		}
	  		  		if (this.hasSubscribe()) {
			names.add(this.subscribe.getClass().getSimpleName());
		}
	  		  		if (this.hasUnsubscribe()) {
			names.add(this.unsubscribe.getClass().getSimpleName());
		}
	  		  		if (this.hasPublish()) {
			names.add(this.publish.getClass().getSimpleName());
		}
	  		  		  		if (this.hasObjectdbStatus()) {
			names.add(this.objectdbStatus.getClass().getSimpleName());
		}
	  		  		if (this.hasObjectdbGet()) {
			names.add(this.objectdbGet.getClass().getSimpleName());
		}
	  		  		if (this.hasObjectdbGetResponse()) {
			names.add(this.objectdbGetResponse.getClass().getSimpleName());
		}
	  		  		  		  		if (this.hasClientConnection()) {
			names.add(this.clientConnection.getClass().getSimpleName());
		}
	  		  		  		  		  		  		  		  		  		  		  		  		if (this.hasPathData()) {
			names.add(this.pathData.getClass().getSimpleName());
		}
	  		  		if (this.hasEchoTest()) {
			names.add(this.echoTest.getClass().getSimpleName());
		}
	  		  		if (this.hasNeighbors()) {
			names.add(this.neighbors.getClass().getSimpleName());
		}
	  		  		  		if (this.hasNeighborsRequest()) {
			names.add(this.neighborsRequest.getClass().getSimpleName());
		}
	  		  		if (this.hasNativeBytes()) {
			names.add(this.nativeBytes.getClass().getSimpleName());
		}
	  		  		  		if (this.hasJsonEntity()) {
			names.add(this.jsonEntity.getClass().getSimpleName());
		}
	  		  		if (this.hasJsonStorage()) {
			names.add(this.jsonStorage.getClass().getSimpleName());
		}
	  		  		if (this.hasRegions()) {
			names.add(this.regions.getClass().getSimpleName());
		}
	  		  		if (this.hasPoisonPill()) {
			names.add(this.poisonPill.getClass().getSimpleName());
		}
	  		  		  		if (this.hasEntityList()) {
			names.add(this.entityList.getClass().getSimpleName());
		}
	  		  			return names;
	}

	    
    public Boolean hasNeighbors()  {
        return neighbors == null ? false : true;
    }
        
		public Neighbors getNeighbors() {
		return neighbors;
	}
	
	public Entity setNeighbors(Neighbors neighbors) {
		this.neighbors = neighbors;
		return this;	}
	
		    
    public Boolean hasChatMessage()  {
        return chatMessage == null ? false : true;
    }
        
		public ChatMessage getChatMessage() {
		return chatMessage;
	}
	
	public Entity setChatMessage(ChatMessage chatMessage) {
		this.chatMessage = chatMessage;
		return this;	}
	
		    
    public Boolean hasClientConnection()  {
        return clientConnection == null ? false : true;
    }
        
		public ClientConnection getClientConnection() {
		return clientConnection;
	}
	
	public Entity setClientConnection(ClientConnection clientConnection) {
		this.clientConnection = clientConnection;
		return this;	}
	
		    
    public Boolean hasEchoTest()  {
        return echoTest == null ? false : true;
    }
        
		public EchoTest getEchoTest() {
		return echoTest;
	}
	
	public Entity setEchoTest(EchoTest echoTest) {
		this.echoTest = echoTest;
		return this;	}
	
		    
    public Boolean hasId()  {
        return id == null ? false : true;
    }
        
		public String getId() {
		return id;
	}
	
	public Entity setId(String id) {
		this.id = id;
		return this;	}
	
		    
    public Boolean hasSubscribe()  {
        return subscribe == null ? false : true;
    }
        
		public Subscribe getSubscribe() {
		return subscribe;
	}
	
	public Entity setSubscribe(Subscribe subscribe) {
		this.subscribe = subscribe;
		return this;	}
	
		    
    public Boolean hasPublish()  {
        return publish == null ? false : true;
    }
        
		public Publish getPublish() {
		return publish;
	}
	
	public Entity setPublish(Publish publish) {
		this.publish = publish;
		return this;	}
	
		    
    public Boolean hasChatChannel()  {
        return chatChannel == null ? false : true;
    }
        
		public ChatChannel getChatChannel() {
		return chatChannel;
	}
	
	public Entity setChatChannel(ChatChannel chatChannel) {
		this.chatChannel = chatChannel;
		return this;	}
	
		    
    public Boolean hasJoinChat()  {
        return joinChat == null ? false : true;
    }
        
		public JoinChat getJoinChat() {
		return joinChat;
	}
	
	public Entity setJoinChat(JoinChat joinChat) {
		this.joinChat = joinChat;
		return this;	}
	
		    
    public Boolean hasLeaveChat()  {
        return leaveChat == null ? false : true;
    }
        
		public LeaveChat getLeaveChat() {
		return leaveChat;
	}
	
	public Entity setLeaveChat(LeaveChat leaveChat) {
		this.leaveChat = leaveChat;
		return this;	}
	
		    
    public Boolean hasUnsubscribe()  {
        return unsubscribe == null ? false : true;
    }
        
		public Unsubscribe getUnsubscribe() {
		return unsubscribe;
	}
	
	public Entity setUnsubscribe(Unsubscribe unsubscribe) {
		this.unsubscribe = unsubscribe;
		return this;	}
	
		    
    public Boolean hasChatRegister()  {
        return chatRegister == null ? false : true;
    }
        
		public ChatRegister getChatRegister() {
		return chatRegister;
	}
	
	public Entity setChatRegister(ChatRegister chatRegister) {
		this.chatRegister = chatRegister;
		return this;	}
	
		    
    public Boolean hasChatChannels()  {
        return chatChannels == null ? false : true;
    }
        
		public ChatChannels getChatChannels() {
		return chatChannels;
	}
	
	public Entity setChatChannels(ChatChannels chatChannels) {
		this.chatChannels = chatChannels;
		return this;	}
	
		    
    public Boolean hasErrorMessage()  {
        return errorMessage == null ? false : true;
    }
        
		public ErrorMessage getErrorMessage() {
		return errorMessage;
	}
	
	public Entity setErrorMessage(ErrorMessage errorMessage) {
		this.errorMessage = errorMessage;
		return this;	}
	
		    
    public Boolean hasNeighborsRequest()  {
        return neighborsRequest == null ? false : true;
    }
        
		public NeighborsRequest getNeighborsRequest() {
		return neighborsRequest;
	}
	
	public Entity setNeighborsRequest(NeighborsRequest neighborsRequest) {
		this.neighborsRequest = neighborsRequest;
		return this;	}
	
		    
    public Boolean hasTrackEntity()  {
        return trackEntity == null ? false : true;
    }
        
		public TrackEntity getTrackEntity() {
		return trackEntity;
	}
	
	public Entity setTrackEntity(TrackEntity trackEntity) {
		this.trackEntity = trackEntity;
		return this;	}
	
		    
    public Boolean hasVector3()  {
        return vector3 == null ? false : true;
    }
        
		public GmVector3 getVector3() {
		return vector3;
	}
	
	public Entity setVector3(GmVector3 vector3) {
		this.vector3 = vector3;
		return this;	}
	
		    
    public Boolean hasEntityList()  {
        return entityList == null ? false : true;
    }
        
		public EntityList getEntityList() {
		return entityList;
	}
	
	public Entity setEntityList(EntityList entityList) {
		this.entityList = entityList;
		return this;	}
	
		    
    public Boolean hasPublished()  {
        return published == null ? false : true;
    }
        
		public Boolean getPublished() {
		return published;
	}
	
	public Entity setPublished(Boolean published) {
		this.published = published;
		return this;	}
	
		    
    public Boolean hasEntityType()  {
        return entityType == null ? false : true;
    }
        
		public String getEntityType() {
		return entityType;
	}
	
	public Entity setEntityType(String entityType) {
		this.entityType = entityType;
		return this;	}
	
		    
    public Boolean hasPlayerAuthenticated()  {
        return playerAuthenticated == null ? false : true;
    }
        
		public PlayerAuthenticated getPlayerAuthenticated() {
		return playerAuthenticated;
	}
	
	public Entity setPlayerAuthenticated(PlayerAuthenticated playerAuthenticated) {
		this.playerAuthenticated = playerAuthenticated;
		return this;	}
	
		    
    public Boolean hasPlayerLogout()  {
        return playerLogout == null ? false : true;
    }
        
		public PlayerLogout getPlayerLogout() {
		return playerLogout;
	}
	
	public Entity setPlayerLogout(PlayerLogout playerLogout) {
		this.playerLogout = playerLogout;
		return this;	}
	
		    
    public Boolean hasSendToPlayer()  {
        return sendToPlayer == null ? false : true;
    }
        
		public Boolean getSendToPlayer() {
		return sendToPlayer;
	}
	
	public Entity setSendToPlayer(Boolean sendToPlayer) {
		this.sendToPlayer = sendToPlayer;
		return this;	}
	
		    
    public Boolean hasSubscribers()  {
        return subscribers == null ? false : true;
    }
        
		public Subscribers getSubscribers() {
		return subscribers;
	}
	
	public Entity setSubscribers(Subscribers subscribers) {
		this.subscribers = subscribers;
		return this;	}
	
		    
    public Boolean hasSave()  {
        return save == null ? false : true;
    }
        
		public Boolean getSave() {
		return save;
	}
	
	public Entity setSave(Boolean save) {
		this.save = save;
		return this;	}
	
		    
    public Boolean hasObjectdbGetResponse()  {
        return objectdbGetResponse == null ? false : true;
    }
        
		public ObjectdbGetResponse getObjectdbGetResponse() {
		return objectdbGetResponse;
	}
	
	public Entity setObjectdbGetResponse(ObjectdbGetResponse objectdbGetResponse) {
		this.objectdbGetResponse = objectdbGetResponse;
		return this;	}
	
		    
    public Boolean hasNativeBytes()  {
        return nativeBytes == null ? false : true;
    }
        
		public NativeBytes getNativeBytes() {
		return nativeBytes;
	}
	
	public Entity setNativeBytes(NativeBytes nativeBytes) {
		this.nativeBytes = nativeBytes;
		return this;	}
	
		    
    public Boolean hasObjectdbGet()  {
        return objectdbGet == null ? false : true;
    }
        
		public ObjectdbGet getObjectdbGet() {
		return objectdbGet;
	}
	
	public Entity setObjectdbGet(ObjectdbGet objectdbGet) {
		this.objectdbGet = objectdbGet;
		return this;	}
	
		    
    public Boolean hasJsonEntity()  {
        return jsonEntity == null ? false : true;
    }
        
		public JsonEntity getJsonEntity() {
		return jsonEntity;
	}
	
	public Entity setJsonEntity(JsonEntity jsonEntity) {
		this.jsonEntity = jsonEntity;
		return this;	}
	
		    
    public Boolean hasDestination()  {
        return destination == null ? false : true;
    }
        
		public String getDestination() {
		return destination;
	}
	
	public Entity setDestination(String destination) {
		this.destination = destination;
		return this;	}
	
		    
    public Boolean hasJson()  {
        return json == null ? false : true;
    }
        
		public Boolean getJson() {
		return json;
	}
	
	public Entity setJson(Boolean json) {
		this.json = json;
		return this;	}
	
		    
    public Boolean hasParams()  {
        return params == null ? false : true;
    }
        
		public String getParams() {
		return params;
	}
	
	public Entity setParams(String params) {
		this.params = params;
		return this;	}
	
		    
    public Boolean hasChatStatus()  {
        return chatStatus == null ? false : true;
    }
        
		public ChatStatus getChatStatus() {
		return chatStatus;
	}
	
	public Entity setChatStatus(ChatStatus chatStatus) {
		this.chatStatus = chatStatus;
		return this;	}
	
		    
    public Boolean hasChatBannedList()  {
        return chatBannedList == null ? false : true;
    }
        
		public ChatBannedList getChatBannedList() {
		return chatBannedList;
	}
	
	public Entity setChatBannedList(ChatBannedList chatBannedList) {
		this.chatBannedList = chatBannedList;
		return this;	}
	
		    
    public Boolean hasChatInvite()  {
        return chatInvite == null ? false : true;
    }
        
		public ChatInvite getChatInvite() {
		return chatInvite;
	}
	
	public Entity setChatInvite(ChatInvite chatInvite) {
		this.chatInvite = chatInvite;
		return this;	}
	
		    
    public Boolean hasClientManagerRegister()  {
        return clientManagerRegister == null ? false : true;
    }
        
		public ClientManagerRegister getClientManagerRegister() {
		return clientManagerRegister;
	}
	
	public Entity setClientManagerRegister(ClientManagerRegister clientManagerRegister) {
		this.clientManagerRegister = clientManagerRegister;
		return this;	}
	
		    
    public Boolean hasClientManagerUnregister()  {
        return clientManagerUnregister == null ? false : true;
    }
        
		public ClientManagerUnregister getClientManagerUnregister() {
		return clientManagerUnregister;
	}
	
	public Entity setClientManagerUnregister(ClientManagerUnregister clientManagerUnregister) {
		this.clientManagerUnregister = clientManagerUnregister;
		return this;	}
	
		    
    public Boolean hasClientEvent()  {
        return clientEvent == null ? false : true;
    }
        
		public ClientEvent getClientEvent() {
		return clientEvent;
	}
	
	public Entity setClientEvent(ClientEvent clientEvent) {
		this.clientEvent = clientEvent;
		return this;	}
	
		    
    public Boolean hasClientEvents()  {
        return clientEvents == null ? false : true;
    }
        
		public ClientEvents getClientEvents() {
		return clientEvents;
	}
	
	public Entity setClientEvents(ClientEvents clientEvents) {
		this.clientEvents = clientEvents;
		return this;	}
	
		    
    public Boolean hasJsonStorage()  {
        return jsonStorage == null ? false : true;
    }
        
		public JsonStorage getJsonStorage() {
		return jsonStorage;
	}
	
	public Entity setJsonStorage(JsonStorage jsonStorage) {
		this.jsonStorage = jsonStorage;
		return this;	}
	
		    
    public Boolean hasClientManagerEvent()  {
        return clientManagerEvent == null ? false : true;
    }
        
		public ClientManagerEvent getClientManagerEvent() {
		return clientManagerEvent;
	}
	
	public Entity setClientManagerEvent(ClientManagerEvent clientManagerEvent) {
		this.clientManagerEvent = clientManagerEvent;
		return this;	}
	
		    
    public Boolean hasRegions()  {
        return regions == null ? false : true;
    }
        
		public Regions getRegions() {
		return regions;
	}
	
	public Entity setRegions(Regions regions) {
		this.regions = regions;
		return this;	}
	
		    
    public Boolean hasFastpath()  {
        return fastpath == null ? false : true;
    }
        
		public Boolean getFastpath() {
		return fastpath;
	}
	
	public Entity setFastpath(Boolean fastpath) {
		this.fastpath = fastpath;
		return this;	}
	
		    
    public Boolean hasPoisonPill()  {
        return poisonPill == null ? false : true;
    }
        
		public PoisonPill getPoisonPill() {
		return poisonPill;
	}
	
	public Entity setPoisonPill(PoisonPill poisonPill) {
		this.poisonPill = poisonPill;
		return this;	}
	
		    
    public Boolean hasSenderId()  {
        return senderId == null ? false : true;
    }
        
		public String getSenderId() {
		return senderId;
	}
	
	public Entity setSenderId(String senderId) {
		this.senderId = senderId;
		return this;	}
	
		    
    public Boolean hasAgentTrackData()  {
        return agentTrackData == null ? false : true;
    }
        
		public AgentTrackData getAgentTrackData() {
		return agentTrackData;
	}
	
	public Entity setAgentTrackData(AgentTrackData agentTrackData) {
		this.agentTrackData = agentTrackData;
		return this;	}
	
		    
    public Boolean hasTeamMemberSkill()  {
        return teamMemberSkill == null ? false : true;
    }
        
		public TeamMemberSkill getTeamMemberSkill() {
		return teamMemberSkill;
	}
	
	public Entity setTeamMemberSkill(TeamMemberSkill teamMemberSkill) {
		this.teamMemberSkill = teamMemberSkill;
		return this;	}
	
		    
    public Boolean hasTrackDataUpdate()  {
        return trackDataUpdate == null ? false : true;
    }
        
		public TrackDataUpdate getTrackDataUpdate() {
		return trackDataUpdate;
	}
	
	public Entity setTrackDataUpdate(TrackDataUpdate trackDataUpdate) {
		this.trackDataUpdate = trackDataUpdate;
		return this;	}
	
		    
    public Boolean hasTrackDataResponse()  {
        return trackDataResponse == null ? false : true;
    }
        
		public TrackDataResponse getTrackDataResponse() {
		return trackDataResponse;
	}
	
	public Entity setTrackDataResponse(TrackDataResponse trackDataResponse) {
		this.trackDataResponse = trackDataResponse;
		return this;	}
	
		    
    public Boolean hasMesh()  {
        return mesh == null ? false : true;
    }
        
		public GmMesh getMesh() {
		return mesh;
	}
	
	public Entity setMesh(GmMesh mesh) {
		this.mesh = mesh;
		return this;	}
	
		    
    public Boolean hasPathData()  {
        return pathData == null ? false : true;
    }
        
		public PathData getPathData() {
		return pathData;
	}
	
	public Entity setPathData(PathData pathData) {
		this.pathData = pathData;
		return this;	}
	
		    
    public Boolean hasObjectdbStatus()  {
        return objectdbStatus == null ? false : true;
    }
        
		public ObjectdbStatus getObjectdbStatus() {
		return objectdbStatus;
	}
	
	public Entity setObjectdbStatus(ObjectdbStatus objectdbStatus) {
		this.objectdbStatus = objectdbStatus;
		return this;	}
	
		    
    public Boolean hasGuildAction()  {
        return guildAction == null ? false : true;
    }
        
		public GuildAction getGuildAction() {
		return guildAction;
	}
	
	public Entity setGuildAction(GuildAction guildAction) {
		this.guildAction = guildAction;
		return this;	}
	
		    
    public Boolean hasPvpGameMessage()  {
        return pvpGameMessage == null ? false : true;
    }
        
		public PvpGameMessage getPvpGameMessage() {
		return pvpGameMessage;
	}
	
	public Entity setPvpGameMessage(PvpGameMessage pvpGameMessage) {
		this.pvpGameMessage = pvpGameMessage;
		return this;	}
	
		    
    public Boolean hasPlayer()  {
        return player == null ? false : true;
    }
        
		public Player getPlayer() {
		return player;
	}
	
	public Entity setPlayer(Player player) {
		this.player = player;
		return this;	}
	
		    
    public Boolean hasTrackData()  {
        return trackData == null ? false : true;
    }
        
		public TrackData getTrackData() {
		return trackData;
	}
	
	public Entity setTrackData(TrackData trackData) {
		this.trackData = trackData;
		return this;	}
	
		    
    public Boolean hasDynamicMessage()  {
        return dynamicMessage == null ? false : true;
    }
        
		public DynamicMessage getDynamicMessage() {
		return dynamicMessage;
	}
	
	public Entity setDynamicMessage(DynamicMessage dynamicMessage) {
		this.dynamicMessage = dynamicMessage;
		return this;	}
	
		    
    public Boolean hasPlayerSkills()  {
        return playerSkills == null ? false : true;
    }
        
		public PlayerSkills getPlayerSkills() {
		return playerSkills;
	}
	
	public Entity setPlayerSkills(PlayerSkills playerSkills) {
		this.playerSkills = playerSkills;
		return this;	}
	
		    
    public Boolean hasPlayerItems()  {
        return playerItems == null ? false : true;
    }
        
		public PlayerItems getPlayerItems() {
		return playerItems;
	}
	
	public Entity setPlayerItems(PlayerItems playerItems) {
		this.playerItems = playerItems;
		return this;	}
	
		    
    public Boolean hasPlayers()  {
        return players == null ? false : true;
    }
        
		public Players getPlayers() {
		return players;
	}
	
	public Entity setPlayers(Players players) {
		this.players = players;
		return this;	}
	
		    
    public Boolean hasTestObject()  {
        return testObject == null ? false : true;
    }
        
		public TestObject getTestObject() {
		return testObject;
	}
	
	public Entity setTestObject(TestObject testObject) {
		this.testObject = testObject;
		return this;	}
	
		    
    public Boolean hasRpcMessage()  {
        return rpcMessage == null ? false : true;
    }
        
		public RpcMessage getRpcMessage() {
		return rpcMessage;
	}
	
	public Entity setRpcMessage(RpcMessage rpcMessage) {
		this.rpcMessage = rpcMessage;
		return this;	}
	
		    
    public Boolean hasGameMessage()  {
        return gameMessage == null ? false : true;
    }
        
		public GameMessage getGameMessage() {
		return gameMessage;
	}
	
	public Entity setGameMessage(GameMessage gameMessage) {
		this.gameMessage = gameMessage;
		return this;	}
	
		    
    public Boolean hasGameMessages()  {
        return gameMessages == null ? false : true;
    }
        
		public GameMessages getGameMessages() {
		return gameMessages;
	}
	
	public Entity setGameMessages(GameMessages gameMessages) {
		this.gameMessages = gameMessages;
		return this;	}
	
	
  
    // java serialization

    public void readExternal(ObjectInput in) throws IOException
    {
        GraphIOUtil.mergeDelimitedFrom(in, this, this);
    }

    public void writeExternal(ObjectOutput out) throws IOException
    {
        GraphIOUtil.writeDelimitedTo(out, this, this);
    }

    // message method

    public Schema<Entity> cachedSchema()
    {
        return DEFAULT_INSTANCE;
    }

    // schema methods

    public Entity newMessage()
    {
        return new Entity();
    }

    public Class<Entity> typeClass()
    {
        return Entity.class;
    }

    public String messageName()
    {
        return Entity.class.getSimpleName();
    }

    public String messageFullName()
    {
        return Entity.class.getName();
    }

    public boolean isInitialized(Entity message)
    {
        return true;
    }

    public void mergeFrom(Input input, Entity message) throws IOException
    {
        for(int number = input.readFieldNumber(this);; number = input.readFieldNumber(this))
        {
            switch(number)
            {
                case 0:
                    return;
                            	case 2:
            	                	                	message.neighbors = input.mergeObject(message.neighbors, Neighbors.getSchema());
                    break;
                                    	
                            	            	case 4:
            	                	                	message.chatMessage = input.mergeObject(message.chatMessage, ChatMessage.getSchema());
                    break;
                                    	
                            	            	case 5:
            	                	                	message.clientConnection = input.mergeObject(message.clientConnection, ClientConnection.getSchema());
                    break;
                                    	
                            	            	case 6:
            	                	                	message.echoTest = input.mergeObject(message.echoTest, EchoTest.getSchema());
                    break;
                                    	
                            	            	case 7:
            	                	                	message.id = input.readString();
                	break;
                	                	
                            	            	case 9:
            	                	                	message.subscribe = input.mergeObject(message.subscribe, Subscribe.getSchema());
                    break;
                                    	
                            	            	case 10:
            	                	                	message.publish = input.mergeObject(message.publish, Publish.getSchema());
                    break;
                                    	
                            	            	case 11:
            	                	                	message.chatChannel = input.mergeObject(message.chatChannel, ChatChannel.getSchema());
                    break;
                                    	
                            	            	case 12:
            	                	                	message.joinChat = input.mergeObject(message.joinChat, JoinChat.getSchema());
                    break;
                                    	
                            	            	case 13:
            	                	                	message.leaveChat = input.mergeObject(message.leaveChat, LeaveChat.getSchema());
                    break;
                                    	
                            	            	case 14:
            	                	                	message.unsubscribe = input.mergeObject(message.unsubscribe, Unsubscribe.getSchema());
                    break;
                                    	
                            	            	case 15:
            	                	                	message.chatRegister = input.mergeObject(message.chatRegister, ChatRegister.getSchema());
                    break;
                                    	
                            	            	case 16:
            	                	                	message.chatChannels = input.mergeObject(message.chatChannels, ChatChannels.getSchema());
                    break;
                                    	
                            	            	case 17:
            	                	                	message.errorMessage = input.mergeObject(message.errorMessage, ErrorMessage.getSchema());
                    break;
                                    	
                            	            	case 21:
            	                	                	message.neighborsRequest = input.mergeObject(message.neighborsRequest, NeighborsRequest.getSchema());
                    break;
                                    	
                            	            	case 22:
            	                	                	message.trackEntity = input.mergeObject(message.trackEntity, TrackEntity.getSchema());
                    break;
                                    	
                            	            	case 25:
            	                	                	message.vector3 = input.mergeObject(message.vector3, GmVector3.getSchema());
                    break;
                                    	
                            	            	case 27:
            	                	                	message.entityList = input.mergeObject(message.entityList, EntityList.getSchema());
                    break;
                                    	
                            	            	case 29:
            	                	                	message.published = input.readBool();
                	break;
                	                	
                            	            	case 30:
            	                	                	message.entityType = input.readString();
                	break;
                	                	
                            	            	case 37:
            	                	                	message.playerAuthenticated = input.mergeObject(message.playerAuthenticated, PlayerAuthenticated.getSchema());
                    break;
                                    	
                            	            	case 38:
            	                	                	message.playerLogout = input.mergeObject(message.playerLogout, PlayerLogout.getSchema());
                    break;
                                    	
                            	            	case 39:
            	                	                	message.sendToPlayer = input.readBool();
                	break;
                	                	
                            	            	case 41:
            	                	                	message.subscribers = input.mergeObject(message.subscribers, Subscribers.getSchema());
                    break;
                                    	
                            	            	case 42:
            	                	                	message.save = input.readBool();
                	break;
                	                	
                            	            	case 44:
            	                	                	message.objectdbGetResponse = input.mergeObject(message.objectdbGetResponse, ObjectdbGetResponse.getSchema());
                    break;
                                    	
                            	            	case 45:
            	                	                	message.nativeBytes = input.mergeObject(message.nativeBytes, NativeBytes.getSchema());
                    break;
                                    	
                            	            	case 46:
            	                	                	message.objectdbGet = input.mergeObject(message.objectdbGet, ObjectdbGet.getSchema());
                    break;
                                    	
                            	            	case 47:
            	                	                	message.jsonEntity = input.mergeObject(message.jsonEntity, JsonEntity.getSchema());
                    break;
                                    	
                            	            	case 48:
            	                	                	message.destination = input.readString();
                	break;
                	                	
                            	            	case 49:
            	                	                	message.json = input.readBool();
                	break;
                	                	
                            	            	case 50:
            	                	                	message.params = input.readString();
                	break;
                	                	
                            	            	case 51:
            	                	                	message.chatStatus = input.mergeObject(message.chatStatus, ChatStatus.getSchema());
                    break;
                                    	
                            	            	case 52:
            	                	                	message.chatBannedList = input.mergeObject(message.chatBannedList, ChatBannedList.getSchema());
                    break;
                                    	
                            	            	case 53:
            	                	                	message.chatInvite = input.mergeObject(message.chatInvite, ChatInvite.getSchema());
                    break;
                                    	
                            	            	case 54:
            	                	                	message.clientManagerRegister = input.mergeObject(message.clientManagerRegister, ClientManagerRegister.getSchema());
                    break;
                                    	
                            	            	case 55:
            	                	                	message.clientManagerUnregister = input.mergeObject(message.clientManagerUnregister, ClientManagerUnregister.getSchema());
                    break;
                                    	
                            	            	case 56:
            	                	                	message.clientEvent = input.mergeObject(message.clientEvent, ClientEvent.getSchema());
                    break;
                                    	
                            	            	case 57:
            	                	                	message.clientEvents = input.mergeObject(message.clientEvents, ClientEvents.getSchema());
                    break;
                                    	
                            	            	case 58:
            	                	                	message.jsonStorage = input.mergeObject(message.jsonStorage, JsonStorage.getSchema());
                    break;
                                    	
                            	            	case 59:
            	                	                	message.clientManagerEvent = input.mergeObject(message.clientManagerEvent, ClientManagerEvent.getSchema());
                    break;
                                    	
                            	            	case 60:
            	                	                	message.regions = input.mergeObject(message.regions, Regions.getSchema());
                    break;
                                    	
                            	            	case 61:
            	                	                	message.fastpath = input.readBool();
                	break;
                	                	
                            	            	case 62:
            	                	                	message.poisonPill = input.mergeObject(message.poisonPill, PoisonPill.getSchema());
                    break;
                                    	
                            	            	case 63:
            	                	                	message.senderId = input.readString();
                	break;
                	                	
                            	            	case 64:
            	                	                	message.agentTrackData = input.mergeObject(message.agentTrackData, AgentTrackData.getSchema());
                    break;
                                    	
                            	            	case 65:
            	                	                	message.teamMemberSkill = input.mergeObject(message.teamMemberSkill, TeamMemberSkill.getSchema());
                    break;
                                    	
                            	            	case 66:
            	                	                	message.trackDataUpdate = input.mergeObject(message.trackDataUpdate, TrackDataUpdate.getSchema());
                    break;
                                    	
                            	            	case 67:
            	                	                	message.trackDataResponse = input.mergeObject(message.trackDataResponse, TrackDataResponse.getSchema());
                    break;
                                    	
                            	            	case 68:
            	                	                	message.mesh = input.mergeObject(message.mesh, GmMesh.getSchema());
                    break;
                                    	
                            	            	case 69:
            	                	                	message.pathData = input.mergeObject(message.pathData, PathData.getSchema());
                    break;
                                    	
                            	            	case 70:
            	                	                	message.objectdbStatus = input.mergeObject(message.objectdbStatus, ObjectdbStatus.getSchema());
                    break;
                                    	
                            	            	case 1000:
            	                	                	message.guildAction = input.mergeObject(message.guildAction, GuildAction.getSchema());
                    break;
                                    	
                            	            	case 1001:
            	                	                	message.pvpGameMessage = input.mergeObject(message.pvpGameMessage, PvpGameMessage.getSchema());
                    break;
                                    	
                            	            	case 1002:
            	                	                	message.player = input.mergeObject(message.player, Player.getSchema());
                    break;
                                    	
                            	            	case 1003:
            	                	                	message.trackData = input.mergeObject(message.trackData, TrackData.getSchema());
                    break;
                                    	
                            	            	case 1004:
            	                	                	message.dynamicMessage = input.mergeObject(message.dynamicMessage, DynamicMessage.getSchema());
                    break;
                                    	
                            	            	case 1005:
            	                	                	message.playerSkills = input.mergeObject(message.playerSkills, PlayerSkills.getSchema());
                    break;
                                    	
                            	            	case 1006:
            	                	                	message.playerItems = input.mergeObject(message.playerItems, PlayerItems.getSchema());
                    break;
                                    	
                            	            	case 1007:
            	                	                	message.players = input.mergeObject(message.players, Players.getSchema());
                    break;
                                    	
                            	            	case 1008:
            	                	                	message.testObject = input.mergeObject(message.testObject, TestObject.getSchema());
                    break;
                                    	
                            	            	case 1009:
            	                	                	message.rpcMessage = input.mergeObject(message.rpcMessage, RpcMessage.getSchema());
                    break;
                                    	
                            	            	case 1010:
            	                	                	message.gameMessage = input.mergeObject(message.gameMessage, GameMessage.getSchema());
                    break;
                                    	
                            	            	case 1011:
            	                	                	message.gameMessages = input.mergeObject(message.gameMessages, GameMessages.getSchema());
                    break;
                                    	
                            	            
                default:
                    input.handleUnknownField(number, this);
            }   
        }
    }


    public void writeTo(Output output, Entity message) throws IOException
    {
    	    	
    	    	
    	    	    	if(message.neighbors != null)
    		output.writeObject(2, message.neighbors, Neighbors.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.chatMessage != null)
    		output.writeObject(4, message.chatMessage, ChatMessage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.clientConnection != null)
    		output.writeObject(5, message.clientConnection, ClientConnection.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.echoTest != null)
    		output.writeObject(6, message.echoTest, EchoTest.getSchema(), false);
    	    	
    	            	
    	    	if(message.id == null)
            throw new UninitializedMessageException(message);
    	    	
    	    	    	if(message.id != null)
            output.writeString(7, message.id, false);
    	    	
    	            	
    	    	
    	    	    	if(message.subscribe != null)
    		output.writeObject(9, message.subscribe, Subscribe.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.publish != null)
    		output.writeObject(10, message.publish, Publish.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.chatChannel != null)
    		output.writeObject(11, message.chatChannel, ChatChannel.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.joinChat != null)
    		output.writeObject(12, message.joinChat, JoinChat.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.leaveChat != null)
    		output.writeObject(13, message.leaveChat, LeaveChat.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.unsubscribe != null)
    		output.writeObject(14, message.unsubscribe, Unsubscribe.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.chatRegister != null)
    		output.writeObject(15, message.chatRegister, ChatRegister.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.chatChannels != null)
    		output.writeObject(16, message.chatChannels, ChatChannels.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.errorMessage != null)
    		output.writeObject(17, message.errorMessage, ErrorMessage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.neighborsRequest != null)
    		output.writeObject(21, message.neighborsRequest, NeighborsRequest.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.trackEntity != null)
    		output.writeObject(22, message.trackEntity, TrackEntity.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.vector3 != null)
    		output.writeObject(25, message.vector3, GmVector3.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.entityList != null)
    		output.writeObject(27, message.entityList, EntityList.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.published != null)
            output.writeBool(29, message.published, false);
    	    	
    	            	
    	    	
    	    	    	if(message.entityType != null)
            output.writeString(30, message.entityType, false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerAuthenticated != null)
    		output.writeObject(37, message.playerAuthenticated, PlayerAuthenticated.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerLogout != null)
    		output.writeObject(38, message.playerLogout, PlayerLogout.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.sendToPlayer != null)
            output.writeBool(39, message.sendToPlayer, false);
    	    	
    	            	
    	    	
    	    	    	if(message.subscribers != null)
    		output.writeObject(41, message.subscribers, Subscribers.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.save != null)
            output.writeBool(42, message.save, false);
    	    	
    	            	
    	    	
    	    	    	if(message.objectdbGetResponse != null)
    		output.writeObject(44, message.objectdbGetResponse, ObjectdbGetResponse.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.nativeBytes != null)
    		output.writeObject(45, message.nativeBytes, NativeBytes.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.objectdbGet != null)
    		output.writeObject(46, message.objectdbGet, ObjectdbGet.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.jsonEntity != null)
    		output.writeObject(47, message.jsonEntity, JsonEntity.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.destination != null)
            output.writeString(48, message.destination, false);
    	    	
    	            	
    	    	
    	    	    	if(message.json != null)
            output.writeBool(49, message.json, false);
    	    	
    	            	
    	    	
    	    	    	if(message.params != null)
            output.writeString(50, message.params, false);
    	    	
    	            	
    	    	
    	    	    	if(message.chatStatus != null)
    		output.writeObject(51, message.chatStatus, ChatStatus.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.chatBannedList != null)
    		output.writeObject(52, message.chatBannedList, ChatBannedList.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.chatInvite != null)
    		output.writeObject(53, message.chatInvite, ChatInvite.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.clientManagerRegister != null)
    		output.writeObject(54, message.clientManagerRegister, ClientManagerRegister.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.clientManagerUnregister != null)
    		output.writeObject(55, message.clientManagerUnregister, ClientManagerUnregister.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.clientEvent != null)
    		output.writeObject(56, message.clientEvent, ClientEvent.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.clientEvents != null)
    		output.writeObject(57, message.clientEvents, ClientEvents.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.jsonStorage != null)
    		output.writeObject(58, message.jsonStorage, JsonStorage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.clientManagerEvent != null)
    		output.writeObject(59, message.clientManagerEvent, ClientManagerEvent.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.regions != null)
    		output.writeObject(60, message.regions, Regions.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.fastpath != null)
            output.writeBool(61, message.fastpath, false);
    	    	
    	            	
    	    	
    	    	    	if(message.poisonPill != null)
    		output.writeObject(62, message.poisonPill, PoisonPill.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.senderId != null)
            output.writeString(63, message.senderId, false);
    	    	
    	            	
    	    	
    	    	    	if(message.agentTrackData != null)
    		output.writeObject(64, message.agentTrackData, AgentTrackData.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.teamMemberSkill != null)
    		output.writeObject(65, message.teamMemberSkill, TeamMemberSkill.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.trackDataUpdate != null)
    		output.writeObject(66, message.trackDataUpdate, TrackDataUpdate.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.trackDataResponse != null)
    		output.writeObject(67, message.trackDataResponse, TrackDataResponse.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.mesh != null)
    		output.writeObject(68, message.mesh, GmMesh.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.pathData != null)
    		output.writeObject(69, message.pathData, PathData.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.objectdbStatus != null)
    		output.writeObject(70, message.objectdbStatus, ObjectdbStatus.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.guildAction != null)
    		output.writeObject(1000, message.guildAction, GuildAction.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.pvpGameMessage != null)
    		output.writeObject(1001, message.pvpGameMessage, PvpGameMessage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.player != null)
    		output.writeObject(1002, message.player, Player.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.trackData != null)
    		output.writeObject(1003, message.trackData, TrackData.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.dynamicMessage != null)
    		output.writeObject(1004, message.dynamicMessage, DynamicMessage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerSkills != null)
    		output.writeObject(1005, message.playerSkills, PlayerSkills.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.playerItems != null)
    		output.writeObject(1006, message.playerItems, PlayerItems.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.players != null)
    		output.writeObject(1007, message.players, Players.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.testObject != null)
    		output.writeObject(1008, message.testObject, TestObject.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.rpcMessage != null)
    		output.writeObject(1009, message.rpcMessage, RpcMessage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.gameMessage != null)
    		output.writeObject(1010, message.gameMessage, GameMessage.getSchema(), false);
    	    	
    	            	
    	    	
    	    	    	if(message.gameMessages != null)
    		output.writeObject(1011, message.gameMessages, GameMessages.getSchema(), false);
    	    	
    	            	
    }

	public void dumpObject()
    {
    	System.out.println("START Entity");
    	    	if(this.neighbors != null) {
    		System.out.println("neighbors="+this.neighbors);
    	}
    	    	if(this.chatMessage != null) {
    		System.out.println("chatMessage="+this.chatMessage);
    	}
    	    	if(this.clientConnection != null) {
    		System.out.println("clientConnection="+this.clientConnection);
    	}
    	    	if(this.echoTest != null) {
    		System.out.println("echoTest="+this.echoTest);
    	}
    	    	if(this.id != null) {
    		System.out.println("id="+this.id);
    	}
    	    	if(this.subscribe != null) {
    		System.out.println("subscribe="+this.subscribe);
    	}
    	    	if(this.publish != null) {
    		System.out.println("publish="+this.publish);
    	}
    	    	if(this.chatChannel != null) {
    		System.out.println("chatChannel="+this.chatChannel);
    	}
    	    	if(this.joinChat != null) {
    		System.out.println("joinChat="+this.joinChat);
    	}
    	    	if(this.leaveChat != null) {
    		System.out.println("leaveChat="+this.leaveChat);
    	}
    	    	if(this.unsubscribe != null) {
    		System.out.println("unsubscribe="+this.unsubscribe);
    	}
    	    	if(this.chatRegister != null) {
    		System.out.println("chatRegister="+this.chatRegister);
    	}
    	    	if(this.chatChannels != null) {
    		System.out.println("chatChannels="+this.chatChannels);
    	}
    	    	if(this.errorMessage != null) {
    		System.out.println("errorMessage="+this.errorMessage);
    	}
    	    	if(this.neighborsRequest != null) {
    		System.out.println("neighborsRequest="+this.neighborsRequest);
    	}
    	    	if(this.trackEntity != null) {
    		System.out.println("trackEntity="+this.trackEntity);
    	}
    	    	if(this.vector3 != null) {
    		System.out.println("vector3="+this.vector3);
    	}
    	    	if(this.entityList != null) {
    		System.out.println("entityList="+this.entityList);
    	}
    	    	if(this.published != null) {
    		System.out.println("published="+this.published);
    	}
    	    	if(this.entityType != null) {
    		System.out.println("entityType="+this.entityType);
    	}
    	    	if(this.playerAuthenticated != null) {
    		System.out.println("playerAuthenticated="+this.playerAuthenticated);
    	}
    	    	if(this.playerLogout != null) {
    		System.out.println("playerLogout="+this.playerLogout);
    	}
    	    	if(this.sendToPlayer != null) {
    		System.out.println("sendToPlayer="+this.sendToPlayer);
    	}
    	    	if(this.subscribers != null) {
    		System.out.println("subscribers="+this.subscribers);
    	}
    	    	if(this.save != null) {
    		System.out.println("save="+this.save);
    	}
    	    	if(this.objectdbGetResponse != null) {
    		System.out.println("objectdbGetResponse="+this.objectdbGetResponse);
    	}
    	    	if(this.nativeBytes != null) {
    		System.out.println("nativeBytes="+this.nativeBytes);
    	}
    	    	if(this.objectdbGet != null) {
    		System.out.println("objectdbGet="+this.objectdbGet);
    	}
    	    	if(this.jsonEntity != null) {
    		System.out.println("jsonEntity="+this.jsonEntity);
    	}
    	    	if(this.destination != null) {
    		System.out.println("destination="+this.destination);
    	}
    	    	if(this.json != null) {
    		System.out.println("json="+this.json);
    	}
    	    	if(this.params != null) {
    		System.out.println("params="+this.params);
    	}
    	    	if(this.chatStatus != null) {
    		System.out.println("chatStatus="+this.chatStatus);
    	}
    	    	if(this.chatBannedList != null) {
    		System.out.println("chatBannedList="+this.chatBannedList);
    	}
    	    	if(this.chatInvite != null) {
    		System.out.println("chatInvite="+this.chatInvite);
    	}
    	    	if(this.clientManagerRegister != null) {
    		System.out.println("clientManagerRegister="+this.clientManagerRegister);
    	}
    	    	if(this.clientManagerUnregister != null) {
    		System.out.println("clientManagerUnregister="+this.clientManagerUnregister);
    	}
    	    	if(this.clientEvent != null) {
    		System.out.println("clientEvent="+this.clientEvent);
    	}
    	    	if(this.clientEvents != null) {
    		System.out.println("clientEvents="+this.clientEvents);
    	}
    	    	if(this.jsonStorage != null) {
    		System.out.println("jsonStorage="+this.jsonStorage);
    	}
    	    	if(this.clientManagerEvent != null) {
    		System.out.println("clientManagerEvent="+this.clientManagerEvent);
    	}
    	    	if(this.regions != null) {
    		System.out.println("regions="+this.regions);
    	}
    	    	if(this.fastpath != null) {
    		System.out.println("fastpath="+this.fastpath);
    	}
    	    	if(this.poisonPill != null) {
    		System.out.println("poisonPill="+this.poisonPill);
    	}
    	    	if(this.senderId != null) {
    		System.out.println("senderId="+this.senderId);
    	}
    	    	if(this.agentTrackData != null) {
    		System.out.println("agentTrackData="+this.agentTrackData);
    	}
    	    	if(this.teamMemberSkill != null) {
    		System.out.println("teamMemberSkill="+this.teamMemberSkill);
    	}
    	    	if(this.trackDataUpdate != null) {
    		System.out.println("trackDataUpdate="+this.trackDataUpdate);
    	}
    	    	if(this.trackDataResponse != null) {
    		System.out.println("trackDataResponse="+this.trackDataResponse);
    	}
    	    	if(this.mesh != null) {
    		System.out.println("mesh="+this.mesh);
    	}
    	    	if(this.pathData != null) {
    		System.out.println("pathData="+this.pathData);
    	}
    	    	if(this.objectdbStatus != null) {
    		System.out.println("objectdbStatus="+this.objectdbStatus);
    	}
    	    	if(this.guildAction != null) {
    		System.out.println("guildAction="+this.guildAction);
    	}
    	    	if(this.pvpGameMessage != null) {
    		System.out.println("pvpGameMessage="+this.pvpGameMessage);
    	}
    	    	if(this.player != null) {
    		System.out.println("player="+this.player);
    	}
    	    	if(this.trackData != null) {
    		System.out.println("trackData="+this.trackData);
    	}
    	    	if(this.dynamicMessage != null) {
    		System.out.println("dynamicMessage="+this.dynamicMessage);
    	}
    	    	if(this.playerSkills != null) {
    		System.out.println("playerSkills="+this.playerSkills);
    	}
    	    	if(this.playerItems != null) {
    		System.out.println("playerItems="+this.playerItems);
    	}
    	    	if(this.players != null) {
    		System.out.println("players="+this.players);
    	}
    	    	if(this.testObject != null) {
    		System.out.println("testObject="+this.testObject);
    	}
    	    	if(this.rpcMessage != null) {
    		System.out.println("rpcMessage="+this.rpcMessage);
    	}
    	    	if(this.gameMessage != null) {
    		System.out.println("gameMessage="+this.gameMessage);
    	}
    	    	if(this.gameMessages != null) {
    		System.out.println("gameMessages="+this.gameMessages);
    	}
    	    	System.out.println("END Entity");
    }
    
    public String getFieldName(int number)
    {
        switch(number)
        {
        	        	case 2: return "neighbors";
        	        	case 4: return "chatMessage";
        	        	case 5: return "clientConnection";
        	        	case 6: return "echoTest";
        	        	case 7: return "id";
        	        	case 9: return "subscribe";
        	        	case 10: return "publish";
        	        	case 11: return "chatChannel";
        	        	case 12: return "joinChat";
        	        	case 13: return "leaveChat";
        	        	case 14: return "unsubscribe";
        	        	case 15: return "chatRegister";
        	        	case 16: return "chatChannels";
        	        	case 17: return "errorMessage";
        	        	case 21: return "neighborsRequest";
        	        	case 22: return "trackEntity";
        	        	case 25: return "vector3";
        	        	case 27: return "entityList";
        	        	case 29: return "published";
        	        	case 30: return "entityType";
        	        	case 37: return "playerAuthenticated";
        	        	case 38: return "playerLogout";
        	        	case 39: return "sendToPlayer";
        	        	case 41: return "subscribers";
        	        	case 42: return "save";
        	        	case 44: return "objectdbGetResponse";
        	        	case 45: return "nativeBytes";
        	        	case 46: return "objectdbGet";
        	        	case 47: return "jsonEntity";
        	        	case 48: return "destination";
        	        	case 49: return "json";
        	        	case 50: return "params";
        	        	case 51: return "chatStatus";
        	        	case 52: return "chatBannedList";
        	        	case 53: return "chatInvite";
        	        	case 54: return "clientManagerRegister";
        	        	case 55: return "clientManagerUnregister";
        	        	case 56: return "clientEvent";
        	        	case 57: return "clientEvents";
        	        	case 58: return "jsonStorage";
        	        	case 59: return "clientManagerEvent";
        	        	case 60: return "regions";
        	        	case 61: return "fastpath";
        	        	case 62: return "poisonPill";
        	        	case 63: return "senderId";
        	        	case 64: return "agentTrackData";
        	        	case 65: return "teamMemberSkill";
        	        	case 66: return "trackDataUpdate";
        	        	case 67: return "trackDataResponse";
        	        	case 68: return "mesh";
        	        	case 69: return "pathData";
        	        	case 70: return "objectdbStatus";
        	        	case 1000: return "guildAction";
        	        	case 1001: return "pvpGameMessage";
        	        	case 1002: return "player";
        	        	case 1003: return "trackData";
        	        	case 1004: return "dynamicMessage";
        	        	case 1005: return "playerSkills";
        	        	case 1006: return "playerItems";
        	        	case 1007: return "players";
        	        	case 1008: return "testObject";
        	        	case 1009: return "rpcMessage";
        	        	case 1010: return "gameMessage";
        	        	case 1011: return "gameMessages";
        	            default: return null;
        }
    }

    public int getFieldNumber(String name)
    {
        final Integer number = __fieldMap.get(name);
        return number == null ? 0 : number.intValue();
    }

    private static final java.util.HashMap<String,Integer> __fieldMap = new java.util.HashMap<String,Integer>();
    static
    {
    	    	__fieldMap.put("neighbors", 2);
    	    	__fieldMap.put("chatMessage", 4);
    	    	__fieldMap.put("clientConnection", 5);
    	    	__fieldMap.put("echoTest", 6);
    	    	__fieldMap.put("id", 7);
    	    	__fieldMap.put("subscribe", 9);
    	    	__fieldMap.put("publish", 10);
    	    	__fieldMap.put("chatChannel", 11);
    	    	__fieldMap.put("joinChat", 12);
    	    	__fieldMap.put("leaveChat", 13);
    	    	__fieldMap.put("unsubscribe", 14);
    	    	__fieldMap.put("chatRegister", 15);
    	    	__fieldMap.put("chatChannels", 16);
    	    	__fieldMap.put("errorMessage", 17);
    	    	__fieldMap.put("neighborsRequest", 21);
    	    	__fieldMap.put("trackEntity", 22);
    	    	__fieldMap.put("vector3", 25);
    	    	__fieldMap.put("entityList", 27);
    	    	__fieldMap.put("published", 29);
    	    	__fieldMap.put("entityType", 30);
    	    	__fieldMap.put("playerAuthenticated", 37);
    	    	__fieldMap.put("playerLogout", 38);
    	    	__fieldMap.put("sendToPlayer", 39);
    	    	__fieldMap.put("subscribers", 41);
    	    	__fieldMap.put("save", 42);
    	    	__fieldMap.put("objectdbGetResponse", 44);
    	    	__fieldMap.put("nativeBytes", 45);
    	    	__fieldMap.put("objectdbGet", 46);
    	    	__fieldMap.put("jsonEntity", 47);
    	    	__fieldMap.put("destination", 48);
    	    	__fieldMap.put("json", 49);
    	    	__fieldMap.put("params", 50);
    	    	__fieldMap.put("chatStatus", 51);
    	    	__fieldMap.put("chatBannedList", 52);
    	    	__fieldMap.put("chatInvite", 53);
    	    	__fieldMap.put("clientManagerRegister", 54);
    	    	__fieldMap.put("clientManagerUnregister", 55);
    	    	__fieldMap.put("clientEvent", 56);
    	    	__fieldMap.put("clientEvents", 57);
    	    	__fieldMap.put("jsonStorage", 58);
    	    	__fieldMap.put("clientManagerEvent", 59);
    	    	__fieldMap.put("regions", 60);
    	    	__fieldMap.put("fastpath", 61);
    	    	__fieldMap.put("poisonPill", 62);
    	    	__fieldMap.put("senderId", 63);
    	    	__fieldMap.put("agentTrackData", 64);
    	    	__fieldMap.put("teamMemberSkill", 65);
    	    	__fieldMap.put("trackDataUpdate", 66);
    	    	__fieldMap.put("trackDataResponse", 67);
    	    	__fieldMap.put("mesh", 68);
    	    	__fieldMap.put("pathData", 69);
    	    	__fieldMap.put("objectdbStatus", 70);
    	    	__fieldMap.put("guildAction", 1000);
    	    	__fieldMap.put("pvpGameMessage", 1001);
    	    	__fieldMap.put("player", 1002);
    	    	__fieldMap.put("trackData", 1003);
    	    	__fieldMap.put("dynamicMessage", 1004);
    	    	__fieldMap.put("playerSkills", 1005);
    	    	__fieldMap.put("playerItems", 1006);
    	    	__fieldMap.put("players", 1007);
    	    	__fieldMap.put("testObject", 1008);
    	    	__fieldMap.put("rpcMessage", 1009);
    	    	__fieldMap.put("gameMessage", 1010);
    	    	__fieldMap.put("gameMessages", 1011);
    	    }
   
   public static List<String> getFields() {
	ArrayList<String> fieldNames = new ArrayList<String>();
	String fieldName = null;
	Integer i = 1;
	
    while(true) { 
		fieldName = Entity.getSchema().getFieldName(i);
		if (fieldName == null) {
			break;
		}
		fieldNames.add(fieldName);
		i++;
	}
	return fieldNames;
}

public static Entity parseFrom(byte[] bytes) {
	Entity message = new Entity();
	ProtobufIOUtil.mergeFrom(bytes, message, Entity.getSchema());
	return message;
}

public static Entity parseFromJson(String json) throws IOException {
	byte[] bytes = json.getBytes(Charset.forName("UTF-8"));
	Entity message = new Entity();
	JsonIOUtil.mergeFrom(bytes, message, Entity.getSchema(), false);
	return message;
}

public Entity clone() {
	byte[] bytes = this.toByteArray();
	Entity entity = Entity.parseFrom(bytes);
	return entity;
}
	
public byte[] toByteArray() {
	return toProtobuf();
	//return toJson();
}

public String toJson() {
	boolean numeric = false;
	ByteArrayOutputStream out = new ByteArrayOutputStream();
	try {
		JsonIOUtil.writeTo(out, this, Entity.getSchema(), numeric);
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException("Json encoding failed");
	}
	String json = new String(out.toByteArray(), Charset.forName("UTF-8"));
	return json;
}

public byte[] toPrefixedByteArray() {
	LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
  Schema<Entity> schema = Entity.getSchema();
    
	final ByteArrayOutputStream out = new ByteArrayOutputStream();
    final ProtobufOutput output = new ProtobufOutput(buffer);
    try
    {
    	schema.writeTo(output, this);
        final int size = output.getSize();
        ProtobufOutput.writeRawVarInt32Bytes(out, size);
        final int msgSize = LinkedBuffer.writeTo(out, buffer);
        assert size == msgSize;
        
        buffer.clear();
        return out.toByteArray();
    }
    catch (IOException e)
    {
        throw new RuntimeException("Serializing to a byte array threw an IOException " + 
                "(should never happen).", e);
    }
 
}

public byte[] toProtobuf() {
	LinkedBuffer buffer = LocalLinkedBuffer.get();
	byte[] bytes = null;

	try {
		bytes = ProtobufIOUtil.toByteArray(this, Entity.getSchema(), buffer);
		buffer.clear();
	} catch (Exception e) {
		buffer.clear();
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bytes;
}

public ByteBuf toByteBuf() {
	ByteBuf bb = Unpooled.buffer(512, 2048);
	LinkedBuffer buffer = LinkedBuffer.use(bb.array());

	try {
		ProtobufIOUtil.writeTo(buffer, this, Entity.getSchema());
	} catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException("Protobuf encoding failed "+e.getMessage());
	}
	return bb;
}

}