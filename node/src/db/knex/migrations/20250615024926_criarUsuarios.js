/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.up = function(knex) {
  return knex.schema.createTable('usuarios', (table) => {
    table.increments('id').primary();
    table.string('foto_url'); 
    table.string('nome', 100).notNullable();
    table.string('email', 100).notNullable().unique();
    table.string('senha_hash');
    table.integer('curso_id');
    table.foreign('curso_id').references('id').inTable('cursos');
    table.timestamps(true, true);
    })
};

/**
 * @param { import("knex").Knex } knex
 * @returns { Promise<void> }
 */
exports.down = async function(knex) {
    await knex.schema.dropTable('usuarios').then(() => {
        console.log('apagado marotamente')
    })
};
