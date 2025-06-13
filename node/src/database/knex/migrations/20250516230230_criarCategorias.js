/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function (knex) {
  return knex.schema.createTable('categorias', (table) => {
    table.increments('id')
    table.text('nome')
    table.timestamp('created_at')
      .defaultTo(knex.fn.now())
    table.timestamp('updated_at')
      .defaultTo(knex.fn.now())
  }).then(() => {
    console.log('Criado a tabela de Categoria')
  })
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function (knex) {
  return knex.schema.dropTable('categorias').then(() => {
    console.log('Deletado tabela de categorias')
  })
};
