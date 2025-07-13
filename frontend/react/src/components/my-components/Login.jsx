import { Form, Formik, useField} from "formik";
import { XCircleIcon } from '@heroicons/react/20/solid';
import { useEffect , useState } from "react"
import * as Yup from 'yup';
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";


const MyTextInput = ({ label, ...props }) => {
// useField() returns [formik.getFieldProps(), formik.getFieldMeta()]
// which we can spread on <input>. We can use field meta to show an error
// message if the field is invalid and it has been touched (i.e. visited)
const [field, meta] = useField(props);
return (
    <>
    <div className={`sm:col-span-${props.gridsize}`}>
        <label htmlFor={props.id || props.name} className="block text-sm/6 font-semibold text-white-900">
            {label}
        </label>
        <div className="mt-2">
            <input
            {...field} {...props}
            className="text-input block w-full rounded-md bg-white px-3 py-1.5 text-lg text-zinc-900 outline-1 -outline-offset-1 outline-zinc-300 placeholder:text-zinc-400 focus:outline-2 focus:-outline-offset-2 focus:outline-emerald-600 sm:text-sm/6"
            />
        </div>
    {meta.touched && meta.error ? (
        <>
        <div className="rounded-md bg-red-50 p-2 mt-1 error">
            <div className="flex">
                <div className="shrink-0">
                    <XCircleIcon aria-hidden="true" className="size-5 text-red-400" />
                </div>
                <div className="ml-3">
                    <h3 className="text-sm font-medium text-red-800">{meta.error}</h3>
                </div>
            </div>
        </div>
        </>
    ) : null}
    </div>
    </>
);
};

const LoginForm = () => {
    
    const { login } = useAuth();
    const navigate = useNavigate()

    return(
        <Formik 
            validateOnMount={true}
            validationSchema={
                Yup.object({
                    username:Yup.string()
                                .email("Must be a valid email")
                                .required("Email is required"),
                    password:Yup.string()
                                .max(30,"Password cannot be longer than 30 characters")
                                .required("Password is required")
            }) }
            initialValues={{username: '',password:''}}
            onSubmit={(values,{setSubmitting}) => {

                setSubmitting(true)
                login(values).then(res => {
                    // TODO: navigae to dashboard
                    navigate("/dashboard")
                    console.log("Successfully logged in")
                }).catch(err => {
                    console.log(err)
                }).finally(() => {
                    setSubmitting(false)
                })

            }}>
        {({isValid,isSubmitting,dirty}) => (
            <Form
                id='login'
                className='grid max-w-2xl grid-cols-1 gap-x-6 gap-y-3 sm:grid-cols-3'
            >
                <MyTextInput
                    gridsize="3"
                    label={"Email"}
                    name={"username"}
                    type={"email"}
                    placeholder={"example@mail.com"}
                />
                <MyTextInput
                    gridsize="3"
                    label={"Password"}
                    name={"password"}
                    type={"password"}
                    placeholder={"Type your password"}
                />
                <button
                    type="submit"
                    form='login'
                    disabled={isSubmitting || !(dirty && isValid)}
                    className={`inline-flex w-full justify-center rounded-md px-3 py-2 text-sm font-semibold text-white shadow-xs sm:col-span-3 ${
                        isSubmitting || !(dirty && isValid) 
                        ? 'bg-zinc-400 cursor-not-allowed' 
                        : 'bg-emerald-600 hover:bg-emerald-500 rounded-md'
                    }`}
                    onClick={() => {
                        // Close modal when form is submitted successfully
                        const form = document.getElementById('createCustomer');
                        if (form && form.checkValidity()) {
                        setTimeout(() => {setOpen(false);onClose();}, 500); // Close after a slight delay
                        }
                    }}
                    >
                    Login
                </button>
            </Form>
        )}
        </Formik>
    )
}


const Login = () => {

    const { customer } = useAuth()
    const navigate = useNavigate()

    useEffect(() => {
        if(customer){
            navigate("/dashboard")
        }
    })

    return (
        <>
        <div className="h-full" >
            <div className="flex min-h-full flex-1 flex-col justify-center px-6 py-12 lg:px-8">
                <div className="sm:mx-auto sm:w-full sm:max-w-sm">
                <img
                    alt="Your Company"
                    src="/images/Logo.svg"
                    className="mx-auto h-10 w-auto"
                />
                <h2 className="mt-5 text-center text-2xl/9 font-bold tracking-tight text-white-900">
                    Sign in to your account
                </h2>
                </div>

                <div className="mt-6 sm:mx-auto sm:w-full sm:max-w-sm">
                <LoginForm/>
                <p className="mt-10 text-center text-sm/6 text-white-500">
                    Not a member?{' '}
                    <a href="#" className="font-semibold text-emerald-600 hover:text-emerald-500">
                    Register Now
                    </a>
                </p>
                </div>
            </div>
        </div>
        {/* Global notification live region, render this  manently at the end of the document */}
        {/* <div
        aria-live="assertive"
        className="pointer-events-none fixed inset-0 flex items-end px-4 py-6 sm:items-start sm:p-6"
        >
        <div className="flex w-full flex-col items-center space-y-4 sm:items-end">
                {notifications.map((notification) => (
                        <Notification 
                            key={notification.id}
                            text={notification.message}
                            type={notification.type}
                        />
                    ))}
        </div>
        </div> */}
        </>
    )
}

export default Login;