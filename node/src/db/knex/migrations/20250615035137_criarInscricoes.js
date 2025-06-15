/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
    return knex.schema.createTable('inscricoes', (table) => {
    table.increments('id').primary();
    table.foreign('usuario_id').references('id').inTable('usuarios');
    table.foreign('evento_id').references('id').inTable('eventos');
    table.timestamp('data_inscricao').defaultTo(knex.fn.now());
    table.boolean('aprovado').defaultTo(false);
    table.unique(['usuario_id', 'evento_id']);
    }).then(() => {
    console.log('Criado a tabela de Usuario')
  })  
    
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = function(knex) {
  
};
