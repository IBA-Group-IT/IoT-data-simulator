/**
  Fields Bindings
*/

const onBlur = field => (e) => {
  e.preventDefault();
  field.onBlur();
  field.validate();
};

export default {

  // MaterialTextField: {
  //   id: 'id',
  //   name: 'name',
  //   type: 'type',
  //   value: 'value',
  //   label: 'label',
  //   placeholder: 'placeholder',
  //   disabled: 'disabled',
  //   error: 'error',
  //   onChange: 'onChange',
  //   onBlur: 'onBlur',
  //   onFocus: 'onFocus',
  //   autoFocus: 'autoFocus',
  // },

   MaterialTextField: ({ $try, field, props }) => ({
    type: $try(props.type, field.type),
    id: $try(props.id, field.id),
    name: $try(props.name, field.name),
    value: $try(props.value, field.value),
    label: $try(props.label, field.label),
    placeholder: $try(props.placeholder, field.placeholder),
    error: !!$try(props.error, field.error),
    disabled: $try(props.disabled, field.disabled),
    onChange: $try(props.onChange, field.onChange),
    onBlur: $try(props.onBlur, onBlur(field)),
    onFocus: $try(props.onFocus, field.onFocus),
    autoFocus: $try(props.autoFocus, field.autoFocus),
  }),

  MaterialSelect: ({ $try, field, props }) => ({
    type: $try(props.type, field.type),
    id: $try(props.id, field.id),
    name: $try(props.name, field.name),
    value: $try(props.value, field.value),
    label: $try(props.label, field.label),
    error: !!$try(props.error, field.error),
    disabled: $try(props.disabled, field.disabled),
    onChange: $try(props.onChange, field.onChange),
    onBlur: $try(props.onBlur, field.onBlur),
    onFocus: $try(props.onFocus, field.onFocus),
  })

};