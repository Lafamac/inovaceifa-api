export default function FormInput({
                                      label,
                                      onKeyDown,
                                      onChange,
                                      uppercase = false,
                                      ...props
                                  }) {
    function handleKeyDown(e) {
        // ENTER vira TAB
        if (e.key === 'Enter') {
            e.preventDefault()

            const form = e.target.form
            const index = Array.prototype.indexOf.call(form, e.target)
            const nextField = form.elements[index + 1]

            if (nextField) {
                nextField.focus()
            }
        }

        // mantém comportamento original se existir
        if (onKeyDown) {
            onKeyDown(e)
        }
    }

    function handleChange(e) {
        if (uppercase) {
            e.target.value = e.target.value.toUpperCase()
        }

        if (onChange) {
            onChange(e)
        }
    }

    return (
        <div className="form-group">
            {label && <label>{label}</label>}
            <input
                {...props}
                onKeyDown={handleKeyDown}
                onChange={handleChange}
            />
        </div>
    )
}
