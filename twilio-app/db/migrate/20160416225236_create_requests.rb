class CreateRequests < ActiveRecord::Migration
  def change
    create_table :requests do |t|
      t.string :message
      t.string :phone

      t.timestamps null: false
    end
  end
end
