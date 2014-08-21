module Tutorial
  class SqlStore
    include GameMachine
    
    def all_for_player(player_id)
      MessageLib::PlayerItem.orm_find_all(player_id)
    end

    def find_by_id(id,player_id,in_transaction=false)
      if in_transaction
        MessageLib::PlayerItem.orm_find(id,player_id,true)
      else
        MessageLib::PlayerItem.orm_find(id,player_id)
      end
    end

    def save_player_item(player_item,player_id,in_transaction=false)
      if in_transaction
        player_item.orm_save(player_id,true)
      else
        player_item.orm_save_async(player_id)
      end
    end

    def delete_player_item(id,player_id)
      MessageLib::PlayerItem.orm_delete_async(id,player_id)
    end

  end
end